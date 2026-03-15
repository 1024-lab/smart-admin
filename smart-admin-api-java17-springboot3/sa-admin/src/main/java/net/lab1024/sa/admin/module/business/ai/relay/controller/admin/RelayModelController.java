package net.lab1024.sa.admin.module.business.ai.relay.controller.admin;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import net.lab1024.sa.admin.module.business.ai.relay.domain.form.RelayModelAddForm;
import net.lab1024.sa.admin.module.business.ai.relay.domain.form.RelayModelQueryForm;
import net.lab1024.sa.admin.module.business.ai.relay.domain.form.RelayModelUpdateForm;
import net.lab1024.sa.admin.module.business.ai.relay.domain.vo.RelayModelVO;
import net.lab1024.sa.admin.module.business.ai.relay.service.admin.RelayModelAdminService;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Relay-模型管理")
public class RelayModelController {

    @Resource
    private RelayModelAdminService relayModelAdminService;

    @Operation(summary = "分页查询")
    @PostMapping("/relay/model/queryPage")
    @SaCheckPermission("relay:model:query")
    public ResponseDTO<PageResult<RelayModelVO>> queryPage(@RequestBody @Valid RelayModelQueryForm queryForm) {
        return ResponseDTO.ok(relayModelAdminService.queryPage(queryForm));
    }

    @Operation(summary = "添加模型")
    @PostMapping("/relay/model/add")
    @SaCheckPermission("relay:model:add")
    public ResponseDTO<String> add(@RequestBody @Valid RelayModelAddForm addForm) {
        return relayModelAdminService.add(addForm);
    }

    @Operation(summary = "更新模型")
    @PostMapping("/relay/model/update")
    @SaCheckPermission("relay:model:update")
    public ResponseDTO<String> update(@RequestBody @Valid RelayModelUpdateForm updateForm) {
        return relayModelAdminService.update(updateForm);
    }

    @Operation(summary = "删除模型")
    @GetMapping("/relay/model/delete/{modelId}")
    @SaCheckPermission("relay:model:delete")
    public ResponseDTO<String> delete(@PathVariable Long modelId) {
        return relayModelAdminService.delete(modelId);
    }

    @Operation(summary = "启用/禁用模型")
    @GetMapping("/relay/model/updateDisabled/{modelId}/{disabledFlag}")
    @SaCheckPermission("relay:model:update")
    public ResponseDTO<String> updateDisabledFlag(@PathVariable Long modelId, @PathVariable Integer disabledFlag) {
        return relayModelAdminService.updateDisabledFlag(modelId, disabledFlag);
    }
}
