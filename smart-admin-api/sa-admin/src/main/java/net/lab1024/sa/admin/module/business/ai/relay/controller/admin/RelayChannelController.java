package net.lab1024.sa.admin.module.business.ai.relay.controller.admin;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import net.lab1024.sa.admin.module.business.ai.relay.domain.form.RelayChannelAddForm;
import net.lab1024.sa.admin.module.business.ai.relay.domain.form.RelayChannelModelBindForm;
import net.lab1024.sa.admin.module.business.ai.relay.domain.form.RelayChannelQueryForm;
import net.lab1024.sa.admin.module.business.ai.relay.domain.form.RelayChannelUpdateForm;
import net.lab1024.sa.admin.module.business.ai.relay.domain.vo.RelayChannelVO;
import net.lab1024.sa.admin.module.business.ai.relay.service.admin.RelayChannelAdminService;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Relay-渠道管理")
public class RelayChannelController {

    @Resource
    private RelayChannelAdminService relayChannelAdminService;

    @Operation(summary = "分页查询")
    @PostMapping("/relay/channel/queryPage")
    @SaCheckPermission("relay:channel:query")
    public ResponseDTO<PageResult<RelayChannelVO>> queryPage(@RequestBody @Valid RelayChannelQueryForm queryForm) {
        return ResponseDTO.ok(relayChannelAdminService.queryPage(queryForm));
    }

    @Operation(summary = "查询列表")
    @PostMapping("/relay/channel/queryList")
    @SaCheckPermission("relay:channel:query")
    public ResponseDTO<List<RelayChannelVO>> queryList(@RequestBody RelayChannelQueryForm queryForm) {
        return ResponseDTO.ok(relayChannelAdminService.queryList(queryForm));
    }

    @Operation(summary = "添加渠道")
    @PostMapping("/relay/channel/add")
    @SaCheckPermission("relay:channel:add")
    public ResponseDTO<String> add(@RequestBody @Valid RelayChannelAddForm addForm) {
        return relayChannelAdminService.add(addForm);
    }

    @Operation(summary = "更新渠道")
    @PostMapping("/relay/channel/update")
    @SaCheckPermission("relay:channel:update")
    public ResponseDTO<String> update(@RequestBody @Valid RelayChannelUpdateForm updateForm) {
        return relayChannelAdminService.update(updateForm);
    }

    @Operation(summary = "删除渠道")
    @GetMapping("/relay/channel/delete/{channelId}")
    @SaCheckPermission("relay:channel:delete")
    public ResponseDTO<String> delete(@PathVariable Long channelId) {
        return relayChannelAdminService.delete(channelId);
    }

    @Operation(summary = "启用/禁用渠道")
    @GetMapping("/relay/channel/updateDisabled/{channelId}/{disabledFlag}")
    @SaCheckPermission("relay:channel:update")
    public ResponseDTO<String> updateDisabledFlag(@PathVariable Long channelId, @PathVariable Integer disabledFlag) {
        return relayChannelAdminService.updateDisabledFlag(channelId, disabledFlag);
    }

    @Operation(summary = "绑定模型")
    @PostMapping("/relay/channel/bindModels")
    @SaCheckPermission("relay:channel:update")
    public ResponseDTO<String> bindModels(@RequestBody @Valid RelayChannelModelBindForm form) {
        return relayChannelAdminService.bindModels(form);
    }

    @Operation(summary = "查询渠道已绑定的模型ID列表")
    @GetMapping("/relay/channel/boundModelIds/{channelId}")
    @SaCheckPermission("relay:channel:query")
    public ResponseDTO<List<Long>> getBoundModelIds(@PathVariable Long channelId) {
        return ResponseDTO.ok(relayChannelAdminService.getBoundModelIds(channelId));
    }
}
