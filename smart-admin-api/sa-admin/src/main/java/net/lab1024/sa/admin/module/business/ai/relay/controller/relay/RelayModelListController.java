package net.lab1024.sa.admin.module.business.ai.relay.controller.relay;

import net.lab1024.sa.admin.module.business.ai.relay.service.relay.RelayModelListService;
import net.lab1024.sa.base.common.annoation.NoNeedLogin;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;

/**
 * OpenAI 兼容 Models 列表端点
 */
@RestController
public class RelayModelListController {

    @Resource
    private RelayModelListService relayModelListService;

    @NoNeedLogin
    @GetMapping(value = "/v1/models", produces = MediaType.APPLICATION_JSON_VALUE)
    public String listModels(HttpServletResponse response) {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        return relayModelListService.listModels();
    }
}
