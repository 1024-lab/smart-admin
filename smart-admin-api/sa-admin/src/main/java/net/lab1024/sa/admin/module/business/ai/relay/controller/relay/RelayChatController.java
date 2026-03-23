package net.lab1024.sa.admin.module.business.ai.relay.controller.relay;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import net.lab1024.sa.admin.module.business.ai.relay.service.relay.RelayChatService;
import net.lab1024.sa.base.common.annoation.NoNeedLogin;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;

/**
 * OpenAI 兼容 Chat Completions 端点
 */
@RestController
public class RelayChatController {

    @Resource
    private RelayChatService relayChatService;

    @NoNeedLogin
    @PostMapping(value = "/v1/chat/completions", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object chatCompletions(@RequestBody String requestBody, HttpServletResponse response) {
        // 判断是否流式
        boolean stream = false;
        try {
            JSONObject json = JSON.parseObject(requestBody);
            stream = json.getBooleanValue("stream", false);
        } catch (Exception ignored) {
        }

        if (stream) {
            response.setContentType(MediaType.TEXT_EVENT_STREAM_VALUE);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Connection", "keep-alive");
            response.setHeader("X-Accel-Buffering", "no");
            return relayChatService.chatCompletionsStream(requestBody);
        }

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        return relayChatService.chatCompletions(requestBody);
    }
}
