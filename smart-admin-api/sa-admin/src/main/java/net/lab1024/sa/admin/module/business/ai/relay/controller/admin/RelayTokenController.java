package net.lab1024.sa.admin.module.business.ai.relay.controller.admin;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import net.lab1024.sa.admin.module.business.ai.relay.domain.form.RelayTokenAddForm;
import net.lab1024.sa.admin.module.business.ai.relay.domain.form.RelayTokenQueryForm;
import net.lab1024.sa.admin.module.business.ai.relay.domain.form.RelayTokenUpdateForm;
import net.lab1024.sa.admin.module.business.ai.relay.domain.vo.RelayTokenCreateResultVO;
import net.lab1024.sa.admin.module.business.ai.relay.domain.vo.RelayTokenVO;
import net.lab1024.sa.admin.module.business.ai.relay.service.admin.RelayTokenAdminService;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Relay-Token管理")
public class RelayTokenController {

    @Resource
    private RelayTokenAdminService relayTokenAdminService;

    @Operation(summary = "分页查询")
    @PostMapping("/relay/token/queryPage")
    @SaCheckPermission("relay:token:query")
    public ResponseDTO<PageResult<RelayTokenVO>> queryPage(@RequestBody @Valid RelayTokenQueryForm queryForm) {
        return ResponseDTO.ok(relayTokenAdminService.queryPage(queryForm));
    }

    @Operation(summary = "创建Token")
    @PostMapping("/relay/token/add")
    @SaCheckPermission("relay:token:add")
    public ResponseDTO<RelayTokenCreateResultVO> add(@RequestBody @Valid RelayTokenAddForm addForm) {
        return relayTokenAdminService.add(addForm);
    }

    @Operation(summary = "更新Token")
    @PostMapping("/relay/token/update")
    @SaCheckPermission("relay:token:update")
    public ResponseDTO<String> update(@RequestBody @Valid RelayTokenUpdateForm updateForm) {
        return relayTokenAdminService.update(updateForm);
    }

    @Operation(summary = "删除Token")
    @GetMapping("/relay/token/delete/{tokenId}")
    @SaCheckPermission("relay:token:delete")
    public ResponseDTO<String> delete(@PathVariable Long tokenId) {
        return relayTokenAdminService.delete(tokenId);
    }
}
