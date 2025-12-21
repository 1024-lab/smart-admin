package net.lab1024.sa.admin.module.business.ai.provider;

import net.lab1024.sa.admin.module.business.ai.domain.dto.AiChatContextDTO;
import net.lab1024.sa.admin.module.business.ai.domain.vo.AiChatMessageVO;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * AI Chat供应商接口
 *
 * @Author Mission
 * @Date 2025-12-21
 * @Copyright Mission
 */
public interface AiChatProvider {

    /**
     * 获取供应商类型
     */
    String getProviderType();

    /**
     * 流式聊天（SSE）
     */
    void chatStreaming(AiChatContextDTO context, SseEmitter emitter);

    /**
     * 阻塞式聊天
     */
    AiChatMessageVO chatBlocking(AiChatContextDTO context);
}
