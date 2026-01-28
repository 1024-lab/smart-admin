package net.lab1024.sa.admin.module.business.ai.provider.impl;

import com.alibaba.fastjson.JSON;
import io.github.imfangs.dify.client.DifyChatClient;
import io.github.imfangs.dify.client.DifyChatflowClient;
import io.github.imfangs.dify.client.DifyClientFactory;
import io.github.imfangs.dify.client.callback.ChatflowStreamCallback;
import io.github.imfangs.dify.client.enums.FileTransferMethod;
import io.github.imfangs.dify.client.enums.FileType;
import io.github.imfangs.dify.client.enums.ResponseMode;
import io.github.imfangs.dify.client.event.*;
import io.github.imfangs.dify.client.exception.DifyApiException;
import io.github.imfangs.dify.client.model.chat.ChatMessage;
import io.github.imfangs.dify.client.model.chat.ChatMessageResponse;
import io.github.imfangs.dify.client.model.common.Metadata;
import io.github.imfangs.dify.client.model.file.FileInfo;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.ai.constant.AiProviderTypeEnum;
import net.lab1024.sa.admin.module.business.ai.domain.config.DifyAppConfig;
import net.lab1024.sa.admin.module.business.ai.domain.dto.AiChatContextDTO;
import net.lab1024.sa.admin.module.business.ai.domain.form.AiChatFileForm;
import net.lab1024.sa.admin.module.business.ai.domain.vo.AiChatMessageVO;
import net.lab1024.sa.admin.module.business.ai.provider.AiChatProvider;
import net.lab1024.sa.base.common.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Dify AI供应商实现
 *
 * @Author Mission
 * @Date 2025-12-21
 * @Copyright Mission
 */
@Slf4j
@Component
public class DifyAiChatProvider implements AiChatProvider {

    @Override
    public String getProviderType() {
        return AiProviderTypeEnum.DIFY.getValue();
    }

    @Override
    public void chatStreaming(AiChatContextDTO context, SseEmitter emitter) {
        String baseUrl = resolveBaseUrl(context);
        String apiKey = getDifyApiKey(context);
        ChatflowEmitterCallback callback = new ChatflowEmitterCallback(emitter);

        // 设置超时和完成回调
        emitter.onTimeout(() -> {
            log.warn("Dify 流式响应超时");
            callback.completeWithError(new BusinessException("Dify 流式响应超时"));
        });
        emitter.onCompletion(() -> log.debug("Dify 流式响应完成"));
        emitter.onError((ex) -> {
            log.error("SseEmitter 发生错误", ex);
            callback.completeWithError(ex);
        });

        try {
            ChatMessage chatMessage = buildChatMessage(context, ResponseMode.STREAMING);
            DifyChatflowClient chatflowClient = DifyClientFactory.createChatWorkflowClient(baseUrl, apiKey);
            chatflowClient.sendChatMessageStream(chatMessage, callback);
            // 注意: sendChatMessageStream 可能是同步阻塞的,也可能是异步的
            // 如果是同步的,执行到这里时流已经结束,回调中的 complete() 已经被调用
            // 如果是异步的,需要保持方法不返回,否则 HTTP 连接会关闭
        } catch (DifyApiException e) {
            log.error("Dify流式API调用失败", e);
            callback.completeWithError(new BusinessException("Dify流式API调用失败: " + e.getMessage()));
        } catch (IOException e) {
            log.error("Dify流式通信异常", e);
            callback.completeWithError(new BusinessException("Dify流式通信异常"));
        } catch (Exception e) {
            log.error("Dify流式调用未知异常", e);
            callback.completeWithError(new BusinessException("Dify流式调用失败"));
        }
    }

    @Override
    public AiChatMessageVO chatBlocking(AiChatContextDTO context) {
        String baseUrl = resolveBaseUrl(context);
        String apiKey = getDifyApiKey(context);

        try {
            ChatMessage chatMessage = buildChatMessage(context, ResponseMode.BLOCKING);

            // 使用 DifyChatClient 处理阻塞模式
            DifyChatClient chatClient = DifyClientFactory.createChatClient(baseUrl, apiKey);
            ChatMessageResponse response = chatClient.sendChatMessage(chatMessage);

            if (response == null) {
                throw new BusinessException("Dify阻塞模式返回空响应");
            }

            return convertResponse(response);
        } catch (DifyApiException e) {
            log.error("Dify阻塞API调用失败", e);
            throw new BusinessException("Dify阻塞API调用失败: " + e.getMessage());
        } catch (IOException e) {
            log.error("Dify阻塞通信异常", e);
            throw new BusinessException("Dify阻塞通信异常");
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("Dify阻塞调用未知异常", e);
            throw new BusinessException("Dify阻塞调用失败");
        }
    }

    /**
     * 获取 Dify API Key(优先使用应用级配置)
     */
    private String getDifyApiKey(AiChatContextDTO context) {
        // 1. 优先使用应用级配置中的 API Key
        if (StringUtils.isNotBlank(context.getAppConfigJson())) {
            try {
                DifyAppConfig appConfig = JSON.parseObject(context.getAppConfigJson(), DifyAppConfig.class);
                appConfig.validate();
                return appConfig.getApiKey();
            } catch (Exception e) {
                log.warn("解析 Dify 应用配置失败,尝试使用平台级 API Key", e);
            }
        }

        // 2. 降级使用平台级 API Key(向后兼容)
        if (StringUtils.isNotBlank(context.getApiKey())) {
            return context.getApiKey();
        }

        throw new BusinessException("Dify 配置缺少 API Key");
    }

    /**
     * 解析 BaseUrl
     */
    private String resolveBaseUrl(AiChatContextDTO context) {
        if (context == null || StringUtils.isBlank(context.getBaseUrl())) {
            throw new BusinessException("Dify 配置缺少 BaseUrl");
        }
        return context.getBaseUrl();
    }

    /**
     * 构建 Dify ChatMessage 请求对象
     */
    private ChatMessage buildChatMessage(AiChatContextDTO context, ResponseMode responseMode) {
        if (context == null || StringUtils.isBlank(context.getQuery())) {
            throw new BusinessException("Dify 请求缺少 query 参数");
        }

        ChatMessage.ChatMessageBuilder builder = ChatMessage.builder()
                .query(context.getQuery())
                .responseMode(responseMode)
                .user(StringUtils.isNotBlank(context.getUser()) ? context.getUser() : "default");

        // 可选参数
        if (StringUtils.isNotBlank(context.getConversationId())) {
            builder.conversationId(context.getConversationId());
        }
        if (context.getInputs() != null && !context.getInputs().isEmpty()) {
            builder.inputs(context.getInputs());
        }

        // 文件参数转换
        List<FileInfo> fileInfos = convertFiles(context.getFiles());
        if (!fileInfos.isEmpty()) {
            builder.files(fileInfos);
        }

        return builder.build();
    }

    /**
     * 转换文件列表
     */
    private List<FileInfo> convertFiles(List<AiChatFileForm> files) {
        if (files == null || files.isEmpty()) {
            return Collections.emptyList();
        }

        List<FileInfo> result = new ArrayList<>(files.size());
        for (AiChatFileForm file : files) {
            if (file == null) {
                continue;
            }

            try {
                FileType fileType = FileType.valueOf(file.getType().toUpperCase(Locale.ROOT));
                FileTransferMethod transferMethod = FileTransferMethod.valueOf(
                        file.getTransferMethod().toUpperCase(Locale.ROOT));

                FileInfo.FileInfoBuilder builder = FileInfo.builder()
                        .type(fileType)
                        .transferMethod(transferMethod);

                if (StringUtils.isNotBlank(file.getUrl())) {
                    builder.url(file.getUrl());
                }
                if (StringUtils.isNotBlank(file.getUploadFileId())) {
                    builder.uploadFileId(file.getUploadFileId());
                }

                result.add(builder.build());
            } catch (IllegalArgumentException ex) {
                log.warn("不支持的 Dify 文件配置: {}", JSON.toJSONString(file), ex);
                throw new BusinessException("无效的 Dify 文件配置");
            }
        }

        return result;
    }

    /**
     * 转换阻塞模式响应
     */
    private AiChatMessageVO convertResponse(ChatMessageResponse response) {
        AiChatMessageVO messageVO = new AiChatMessageVO();
        messageVO.setEvent("message");
        messageVO.setMessageId(response.getMessageId());
        messageVO.setConversationId(response.getConversationId());
        messageVO.setMode(response.getMode());
        messageVO.setAnswer(response.getAnswer());
        messageVO.setCreatedAt(response.getCreatedAt());

        if (response.getMetadata() != null) {
            messageVO.setMetadata(convertMetadata(response.getMetadata()));
        }

        return messageVO;
    }

    /**
     * 转换元数据
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> convertMetadata(Metadata metadata) {
        if (metadata == null) {
            return null;
        }
        String jsonStr = JSON.toJSONString(metadata);
        return JSON.parseObject(jsonStr, Map.class);
    }

    /**
     * 转换事件为消息VO
     */
    private AiChatMessageVO convertEventToMessage(BaseEvent event) {
        if (event == null) {
            return null;
        }

        AiChatMessageVO messageVO = new AiChatMessageVO();
        messageVO.setEvent(event.getEvent());
        messageVO.setCreatedAt(event.getCreatedAt());

        // 提取通用的消息ID和会话ID
        if (event instanceof BaseMessageEvent baseMessageEvent) {
            messageVO.setMessageId(baseMessageEvent.getMessageId());
            messageVO.setConversationId(baseMessageEvent.getConversationId());
        }

        // 提取回答内容
        if (event instanceof MessageEvent messageEvent) {
            messageVO.setAnswer(messageEvent.getAnswer());
        } else if (event instanceof MessageReplaceEvent replaceEvent) {
            messageVO.setAnswer(replaceEvent.getAnswer());
        } else if (event instanceof AgentMessageEvent agentMessageEvent) {
            messageVO.setAnswer(agentMessageEvent.getAnswer());
        } else if (event instanceof TtsMessageEvent ttsMessageEvent) {
            messageVO.setAnswer(ttsMessageEvent.getAudio());
        } else if (event instanceof TtsMessageEndEvent ttsMessageEndEvent) {
            messageVO.setAnswer(ttsMessageEndEvent.getAudio());
        } else if (event instanceof WorkflowTextChunkEvent workflowTextChunkEvent) {
            messageVO.setAnswer(workflowTextChunkEvent.getData() != null ?
                    String.valueOf(workflowTextChunkEvent.getData()) : null);
        }

        // 提取元数据
        if (event instanceof MessageEndEvent messageEndEvent) {
            messageVO.setMetadata(convertMetadata(messageEndEvent.getMetadata()));
        } else if (event instanceof AgentThoughtEvent agentThoughtEvent) {
            messageVO.setMetadata(convertObjectToMap(agentThoughtEvent));
        } else if (event instanceof MessageFileEvent messageFileEvent) {
            messageVO.setMetadata(convertObjectToMap(messageFileEvent));
        } else if (event instanceof WorkflowStartedEvent workflowStartedEvent) {
            messageVO.setMetadata(convertObjectToMap(workflowStartedEvent.getData()));
        } else if (event instanceof NodeStartedEvent nodeStartedEvent) {
            messageVO.setMetadata(convertObjectToMap(nodeStartedEvent.getData()));
        } else if (event instanceof NodeFinishedEvent nodeFinishedEvent) {
            messageVO.setMetadata(convertObjectToMap(nodeFinishedEvent.getData()));
        } else if (event instanceof WorkflowFinishedEvent workflowFinishedEvent) {
            messageVO.setMetadata(convertObjectToMap(workflowFinishedEvent.getData()));
        }

        // 提取错误信息
        if (event instanceof ErrorEvent errorEvent) {
            messageVO.setError(errorEvent.getMessage());
        }

        return messageVO;
    }

    /**
     * 将对象转换为Map
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> convertObjectToMap(Object data) {
        if (data == null) {
            return null;
        }
        String jsonStr = JSON.toJSONString(data);
        return JSON.parseObject(jsonStr, Map.class);
    }

    /**
     * SSE Emitter 回调适配器
     */
    private class ChatflowEmitterCallback implements ChatflowStreamCallback {

        private final SseEmitter emitter;
        private final AtomicBoolean completed = new AtomicBoolean(false);

        private ChatflowEmitterCallback(SseEmitter emitter) {
            this.emitter = emitter;
        }

        @Override
        public void onMessage(MessageEvent event) {
            emit(event);
        }

        @Override
        public void onMessageEnd(MessageEndEvent event) {
            emit(event);
            // MessageEndEvent 表示普通聊天消息结束,应该完成流
            complete();
        }

        @Override
        public void onMessageFile(MessageFileEvent event) {
            emit(event);
        }

        @Override
        public void onTTSMessage(TtsMessageEvent event) {
            emit(event);
        }

        @Override
        public void onTTSMessageEnd(TtsMessageEndEvent event) {
            emit(event);
        }

        @Override
        public void onMessageReplace(MessageReplaceEvent event) {
            emit(event);
        }

        @Override
        public void onAgentMessage(AgentMessageEvent event) {
            emit(event);
        }

        @Override
        public void onAgentThought(AgentThoughtEvent event) {
            emit(event);
        }

        @Override
        public void onWorkflowStarted(WorkflowStartedEvent event) {
            emit(event);
        }

        @Override
        public void onNodeStarted(NodeStartedEvent event) {
            emit(event);
        }

        @Override
        public void onNodeFinished(NodeFinishedEvent event) {
            emit(event);
        }

        @Override
        public void onWorkflowFinished(WorkflowFinishedEvent event) {
            emit(event);
            complete();
        }

        @Override
        public void onError(ErrorEvent event) {
            log.error("Dify 流式错误事件: {}", event != null ? event.getMessage() : null);
            emit(event);
            complete();
        }

        @Override
        public void onPing(PingEvent event) {
            // Keep-alive ping,不需要发送给前端
        }

        @Override
        public void onException(Throwable throwable) {
            log.error("Dify 流式异常", throwable);
            completeWithError(throwable);
        }

        /**
         * 完成并返回错误
         */
        void completeWithError(Throwable throwable) {
            if (completed.compareAndSet(false, true)) {
                emitter.completeWithError(throwable);
            }
        }

        /**
         * 正常完成
         */
        private void complete() {
            if (completed.compareAndSet(false, true)) {
                emitter.complete();
            }
        }

        /**
         * 发送事件到 SSE
         */
        private void emit(BaseEvent event) {
            if (event == null) {
                return;
            }

            AiChatMessageVO messageVO = convertEventToMessage(event);
            if (messageVO == null) {
                return;
            }

            try {
                String eventName = StringUtils.isNotBlank(messageVO.getEvent()) ?
                        messageVO.getEvent() : "message";
                emitter.send(SseEmitter.event()
                        .name(eventName)
                        .data(messageVO));
            } catch (IOException e) {
                log.error("发送 SSE 事件失败: {}", messageVO.getEvent(), e);
                completeWithError(e);
            }
        }
    }
}
