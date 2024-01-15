package net.lab1024.sa.admin.module.system.support;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import net.lab1024.sa.base.common.controller.SupportBaseController;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.domain.ValidateList;
import net.lab1024.sa.base.constant.SwaggerTagConst;
import net.lab1024.sa.base.module.support.changelog.domain.form.ChangeLogAddForm;
import net.lab1024.sa.base.module.support.changelog.domain.form.ChangeLogUpdateForm;
import net.lab1024.sa.base.module.support.changelog.service.ChangeLogService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 系统更新日志 Controller
 *
 * @Author 卓大
 * @Date 2022-09-26 14:53:50
 * @Copyright 1024创新实验室
 */

@RestController
@Tag(name = SwaggerTagConst.Support.CHANGE_LOG)
public class AdminChangeLogController extends SupportBaseController {

    @Resource
    private ChangeLogService changeLogService;

    @Operation(summary = "添加 @author 卓大")
    @PostMapping("/changeLog/add")
    @SaCheckPermission("support:changeLog:add")
    public ResponseDTO<String> add(@RequestBody @Valid ChangeLogAddForm addForm) {
        return changeLogService.add(addForm);
    }

    @Operation(summary = "更新 @author 卓大")
    @PostMapping("/changeLog/update")
    @SaCheckPermission("support:changeLog:update")
    public ResponseDTO<String> update(@RequestBody @Valid ChangeLogUpdateForm updateForm) {
        return changeLogService.update(updateForm);
    }

    @Operation(summary = "批量删除 @author 卓大")
    @PostMapping("/changeLog/batchDelete")
    @SaCheckPermission("support:changeLog:batchDelete")
    public ResponseDTO<String> batchDelete(@RequestBody ValidateList<Long> idList) {
        return changeLogService.batchDelete(idList);
    }

    @Operation(summary = "单个删除 @author 卓大")
    @GetMapping("/changeLog/delete/{changeLogId}")
    @SaCheckPermission("support:changeLog:delete")
    public ResponseDTO<String> batchDelete(@PathVariable Long changeLogId) {
        return changeLogService.delete(changeLogId);
    }
}