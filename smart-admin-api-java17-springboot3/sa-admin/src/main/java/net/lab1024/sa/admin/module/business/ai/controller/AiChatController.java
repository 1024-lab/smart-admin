package net.lab1024.sa.admin.module.business.ai.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.ai.constant.AiResponseModeEnum;
import net.lab1024.sa.admin.module.business.ai.domain.form.AiChatMessageForm;
import net.lab1024.sa.admin.module.business.ai.domain.vo.AiChatMessageVO;
import net.lab1024.sa.admin.module.business.ai.service.AiChatService;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * AI Chat Controller
 *
 * @Author Mission
 * @Date 2025-12-21
 * @Copyright Mission
 */
@Slf4j
@RestController
@Tag(name = "AI Chat")
public class AiChatController {

    @Resource
    private AiChatService aiChatService;

    /**
     * 统一Chat消息接口
     */
    @Operation(summary = "发送Chat消息 @author Mission")
    @PostMapping("/ai/chat/messages")
    @SaCheckPermission("ai:chat")
    public Object chatMessages(@RequestBody @Valid AiChatMessageForm form) {

        if (AiResponseModeEnum.STREAMING.getValue().equals(form.getResponseMode())) {
            // 流式响应
            SseEmitter emitter = new SseEmitter(300000L); // 5分钟超时

            emitter.onCompletion(() -> log.info("SSE连接完成"));
            emitter.onTimeout(() -> log.warn("SSE连接超时"));
            emitter.onError(e -> log.error("SSE连接错误", e));

            aiChatService.chatStreaming(form, emitter);
            return emitter;

        } else {
            // 阻塞响应
            AiChatMessageVO result = aiChatService.chatBlocking(form);
            return ResponseDTO.ok(result);
        }
    }
}
