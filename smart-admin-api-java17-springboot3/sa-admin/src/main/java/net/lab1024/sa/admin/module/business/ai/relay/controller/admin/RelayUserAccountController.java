package net.lab1024.sa.admin.module.business.ai.relay.controller.admin;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import net.lab1024.sa.admin.module.business.ai.relay.domain.form.RelayUserAccountQueryForm;
import net.lab1024.sa.admin.module.business.ai.relay.domain.form.RelayUserAccountRechargeForm;
import net.lab1024.sa.admin.module.business.ai.relay.domain.form.RelayUserAccountUpdateForm;
import net.lab1024.sa.admin.module.business.ai.relay.domain.vo.RelayQuotaLedgerVO;
import net.lab1024.sa.admin.module.business.ai.relay.domain.vo.RelayUserAccountVO;
import net.lab1024.sa.admin.module.business.ai.relay.service.admin.RelayUserAccountAdminService;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Relay-用户账户管理")
public class RelayUserAccountController {

    @Resource
    private RelayUserAccountAdminService relayUserAccountAdminService;

    @Operation(summary = "分页查询")
    @PostMapping("/relay/account/queryPage")
    @SaCheckPermission("relay:account:query")
    public ResponseDTO<PageResult<RelayUserAccountVO>> queryPage(@RequestBody @Valid RelayUserAccountQueryForm queryForm) {
        return ResponseDTO.ok(relayUserAccountAdminService.queryPage(queryForm));
    }

    @Operation(summary = "充值")
    @PostMapping("/relay/account/recharge")
    @SaCheckPermission("relay:account:recharge")
    public ResponseDTO<String> recharge(@RequestBody @Valid RelayUserAccountRechargeForm form) {
        return relayUserAccountAdminService.recharge(form);
    }

    @Operation(summary = "启用/禁用账户")
    @GetMapping("/relay/account/updateDisabled/{employeeId}/{disabledFlag}")
    @SaCheckPermission("relay:account:update")
    public ResponseDTO<String> updateDisabledFlag(@PathVariable Long employeeId, @PathVariable Integer disabledFlag) {
        return relayUserAccountAdminService.updateDisabledFlag(employeeId, disabledFlag);
    }

    @Operation(summary = "查询额度流水")
    @GetMapping("/relay/account/ledger/{employeeId}")
    @SaCheckPermission("relay:account:query")
    public ResponseDTO<PageResult<RelayQuotaLedgerVO>> queryLedger(@PathVariable Long employeeId,
                                                                    @RequestParam(defaultValue = "1") Integer pageNum,
                                                                    @RequestParam(defaultValue = "20") Integer pageSize) {
        return ResponseDTO.ok(relayUserAccountAdminService.queryLedger(employeeId, pageNum, pageSize));
    }

    @Operation(summary = "修改账户")
    @PostMapping("/relay/account/update")
    @SaCheckPermission("relay:account:update")
    public ResponseDTO<String> update(@RequestBody @Valid RelayUserAccountUpdateForm form) {
        return relayUserAccountAdminService.update(form);
    }

    @Operation(summary = "删除账户")
    @GetMapping("/relay/account/delete/{employeeId}")
    @SaCheckPermission("relay:account:delete")
    public ResponseDTO<String> delete(@PathVariable Long employeeId) {
        return relayUserAccountAdminService.delete(employeeId);
    }
}
