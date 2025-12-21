package net.lab1024.sa.admin.module.business.ai.service;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.ai.domain.dto.AiChatContextDTO;
import net.lab1024.sa.admin.module.business.ai.domain.entity.AiAppEntity;
import net.lab1024.sa.admin.module.business.ai.domain.entity.AiAppProviderEntity;
import net.lab1024.sa.admin.module.business.ai.domain.form.AiChatMessageForm;
import net.lab1024.sa.admin.module.business.ai.domain.vo.AiChatMessageVO;
import net.lab1024.sa.admin.module.business.ai.provider.AiChatProvider;
import net.lab1024.sa.admin.module.business.ai.provider.AiProviderFactory;
import net.lab1024.sa.base.common.code.UserErrorCode;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * AI Chat服务
 *
 * @Author Mission
 * @Date 2025-12-21
 * @Copyright Mission
 */
@Slf4j
@Service
public class AiChatService {

    @Resource
    private AiProviderFactory providerFactory;

    @Resource
    private AiAppService aiAppService;

    @Resource
    private AiAppProviderService aiAppProviderService;

    /**
     * 流式聊天（异步）
     */
    @Async
    public void chatStreaming(AiChatMessageForm form, SseEmitter emitter) {
        try {
            // 获取配置并调用供应商
            AiChatContextDTO context = buildChatContext(form);
            AiAppProviderEntity provider = getProviderByAppId(form.getAppId());

            AiChatProvider chatProvider = providerFactory.getProvider(provider.getName());
            chatProvider.chatStreaming(context, emitter);

        } catch (Exception e) {
            log.error("流式聊天失败", e);
            emitter.completeWithError(e);
        }
    }

    /**
     * 阻塞式聊天
     */
    public AiChatMessageVO chatBlocking(AiChatMessageForm form) {
        AiChatContextDTO context = buildChatContext(form);
        AiAppProviderEntity provider = getProviderByAppId(form.getAppId());

        AiChatProvider chatProvider = providerFactory.getProvider(provider.getName());
        return chatProvider.chatBlocking(context);
    }

    /**
     * 构建聊天上下文
     */
    private AiChatContextDTO buildChatContext(AiChatMessageForm form) {
        AiAppProviderEntity provider = getProviderByAppId(form.getAppId());

        AiChatContextDTO context = new AiChatContextDTO();
        context.setBaseUrl(provider.getBaseUrl());
        context.setApiKey(provider.getKey());
        context.setQuery(form.getQuery());
        context.setInputs(form.getInputs());
        context.setConversationId(form.getConversationId());
        context.setUser(form.getUser());
        context.setFiles(form.getFiles());

        return context;
    }

    /**
     * 根据appId获取供应商配置
     */
    private AiAppProviderEntity getProviderByAppId(String appId) {
        // 1. 查询应用
        AiAppEntity app = aiAppService.aiAppDao.selectById(appId);
        if (app == null) {
            throw ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "应用不存在").exception();
        }

        // 2. 查询供应商
        AiAppProviderEntity provider = aiAppProviderService.aiAppProviderDao.selectById(app.getAppProviderId());
        if (provider == null) {
            throw ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "供应商不存在").exception();
        }

        return provider;
    }
}
