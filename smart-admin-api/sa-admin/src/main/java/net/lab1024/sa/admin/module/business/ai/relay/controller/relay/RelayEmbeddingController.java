package net.lab1024.sa.admin.module.business.ai.relay.controller.relay;

import net.lab1024.sa.admin.module.business.ai.relay.service.relay.RelayEmbeddingService;
import net.lab1024.sa.base.common.annoation.NoNeedLogin;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;

/**
 * OpenAI 兼容 Embeddings 端点
 */
@RestController
public class RelayEmbeddingController {

    @Resource
    private RelayEmbeddingService relayEmbeddingService;

    @NoNeedLogin
    @PostMapping(value = "/v1/embeddings", produces = MediaType.APPLICATION_JSON_VALUE)
    public String embeddings(@RequestBody String requestBody, HttpServletResponse response) {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        return relayEmbeddingService.embeddings(requestBody);
    }
}
