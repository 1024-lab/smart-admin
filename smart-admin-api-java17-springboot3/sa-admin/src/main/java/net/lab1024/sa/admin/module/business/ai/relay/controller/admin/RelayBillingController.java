package net.lab1024.sa.admin.module.business.ai.relay.controller.admin;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import net.lab1024.sa.admin.module.business.ai.relay.domain.form.RelayBillingSessionQueryForm;
import net.lab1024.sa.admin.module.business.ai.relay.domain.vo.RelayBillingSessionVO;
import net.lab1024.sa.admin.module.business.ai.relay.service.admin.RelayBillingQueryService;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Relay-计费会话查询")
public class RelayBillingController {

    @Resource
    private RelayBillingQueryService relayBillingQueryService;

    @Operation(summary = "分页查询计费会话")
    @PostMapping("/relay/billing/queryPage")
    @SaCheckPermission("relay:billing:query")
    public ResponseDTO<PageResult<RelayBillingSessionVO>> queryPage(@RequestBody @Valid RelayBillingSessionQueryForm queryForm) {
        return ResponseDTO.ok(relayBillingQueryService.queryPage(queryForm));
    }
}
