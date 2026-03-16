package net.lab1024.sa.admin.module.business.ai.relay.controller.admin;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import net.lab1024.sa.admin.module.business.ai.relay.domain.form.RelayChannelGroupAddForm;
import net.lab1024.sa.admin.module.business.ai.relay.domain.form.RelayChannelGroupQueryForm;
import net.lab1024.sa.admin.module.business.ai.relay.domain.form.RelayChannelGroupUpdateForm;
import net.lab1024.sa.admin.module.business.ai.relay.domain.vo.RelayChannelGroupVO;
import net.lab1024.sa.admin.module.business.ai.relay.service.admin.RelayChannelGroupAdminService;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Relay-渠道分组管理")
public class RelayChannelGroupController {

    @Resource
    private RelayChannelGroupAdminService relayChannelGroupAdminService;

    @Operation(summary = "分页查询")
    @PostMapping("/relay/channelGroup/queryPage")
    @SaCheckPermission("relay:channelGroup:query")
    public ResponseDTO<PageResult<RelayChannelGroupVO>> queryPage(@RequestBody @Valid RelayChannelGroupQueryForm queryForm) {
        return ResponseDTO.ok(relayChannelGroupAdminService.queryPage(queryForm));
    }

    @Operation(summary = "新增渠道分组")
    @PostMapping("/relay/channelGroup/add")
    @SaCheckPermission("relay:channelGroup:add")
    public ResponseDTO<String> add(@RequestBody @Valid RelayChannelGroupAddForm addForm) {
        return relayChannelGroupAdminService.add(addForm);
    }

    @Operation(summary = "更新渠道分组")
    @PostMapping("/relay/channelGroup/update")
    @SaCheckPermission("relay:channelGroup:update")
    public ResponseDTO<String> update(@RequestBody @Valid RelayChannelGroupUpdateForm updateForm) {
        return relayChannelGroupAdminService.update(updateForm);
    }

    @Operation(summary = "删除渠道分组")
    @GetMapping("/relay/channelGroup/delete/{channelGroupId}")
    @SaCheckPermission("relay:channelGroup:delete")
    public ResponseDTO<String> delete(@PathVariable Long channelGroupId) {
        return relayChannelGroupAdminService.delete(channelGroupId);
    }

    @Operation(summary = "启用/禁用渠道分组")
    @GetMapping("/relay/channelGroup/updateDisabled/{channelGroupId}/{disabledFlag}")
    @SaCheckPermission("relay:channelGroup:update")
    public ResponseDTO<String> updateDisabledFlag(@PathVariable Long channelGroupId, @PathVariable Integer disabledFlag) {
        return relayChannelGroupAdminService.updateDisabledFlag(channelGroupId, disabledFlag);
    }
}
