package net.lab1024.sa.admin.module.business.ai.relay.controller.admin;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import net.lab1024.sa.admin.module.business.ai.relay.domain.form.RelayUsageLogQueryForm;
import net.lab1024.sa.admin.module.business.ai.relay.domain.vo.RelayUsageLogVO;
import net.lab1024.sa.admin.module.business.ai.relay.service.admin.RelayUsageLogQueryService;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Relay-用量日志查询")
public class RelayUsageLogController {

    @Resource
    private RelayUsageLogQueryService relayUsageLogQueryService;

    @Operation(summary = "分页查询用量日志")
    @PostMapping("/relay/usageLog/queryPage")
    @SaCheckPermission("relay:usageLog:query")
    public ResponseDTO<PageResult<RelayUsageLogVO>> queryPage(@RequestBody @Valid RelayUsageLogQueryForm queryForm) {
        return ResponseDTO.ok(relayUsageLogQueryService.queryPage(queryForm));
    }
}
