package net.lab1024.sa.admin.module.business.ai.provider.impl;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.ai.constant.AiProviderTypeEnum;
import net.lab1024.sa.admin.module.business.ai.domain.dto.AiChatContextDTO;
import net.lab1024.sa.admin.module.business.ai.domain.vo.AiChatMessageVO;
import net.lab1024.sa.admin.module.business.ai.provider.AiChatProvider;
import net.lab1024.sa.base.common.code.UserErrorCode;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

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
        HttpURLConnection connection = null;
        try {
            // 构建请求体
            Map<String, Object> requestBody = buildRequestBody(context, "streaming");

            // 创建HTTP连接
            String url = context.getBaseUrl() + "/v1/chat-messages";
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + context.getApiKey());
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            connection.setReadTimeout(300000); // 5分钟超时

            // 发送请求
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = JSON.toJSONString(requestBody).getBytes(StandardCharsets.UTF_8);
                os.write(input);
            }

            // 读取流式响应
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("data: ")) {
                        String data = line.substring(6).trim();

                        if ("[DONE]".equals(data) || data.isEmpty()) {
                            emitter.complete();
                            break;
                        }

                        try {
                            AiChatMessageVO eventVO = JSON.parseObject(data, AiChatMessageVO.class);
                            emitter.send(SseEmitter.event()
                                .name(eventVO.getEvent() != null ? eventVO.getEvent() : "message")
                                .data(eventVO));
                        } catch (Exception e) {
                            log.error("解析SSE数据失败: {}", data, e);
                        }
                    }
                }
            }

        } catch (IOException e) {
            log.error("Dify流式API调用失败", e);
            emitter.completeWithError(e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    @Override
    public AiChatMessageVO chatBlocking(AiChatContextDTO context) {
        throw ResponseDTO.error(UserErrorCode.DEVELOPING, "暂不支持阻塞模式").exception();
    }

    private Map<String, Object> buildRequestBody(AiChatContextDTO context, String responseMode) {
        Map<String, Object> body = new HashMap<>();
        body.put("query", context.getQuery());
        body.put("response_mode", responseMode);
        body.put("user", context.getUser() != null ? context.getUser() : "default");

        if (context.getInputs() != null) {
            body.put("inputs", context.getInputs());
        }
        if (context.getConversationId() != null) {
            body.put("conversation_id", context.getConversationId());
        }
        if (context.getFiles() != null && !context.getFiles().isEmpty()) {
            body.put("files", context.getFiles());
        }

        return body;
    }
}
