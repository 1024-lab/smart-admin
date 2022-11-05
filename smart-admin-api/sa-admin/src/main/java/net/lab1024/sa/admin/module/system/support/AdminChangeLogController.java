package net.lab1024.sa.admin.module.system.support;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.sa.common.common.domain.ResponseDTO;
import net.lab1024.sa.common.common.domain.ValidateList;
import net.lab1024.sa.common.constant.SwaggerTagConst;
import net.lab1024.sa.common.module.support.changelog.domain.form.ChangeLogAddForm;
import net.lab1024.sa.common.module.support.changelog.domain.form.ChangeLogUpdateForm;
import net.lab1024.sa.common.module.support.changelog.service.ChangeLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 系统更新日志 Controller
 *
 * @Author 卓大
 * @Date 2022-09-26 14:53:50
 * @Copyright 1024创新实验室
 */

@RestController
@Api(tags = SwaggerTagConst.Support.CHANGE_LOG)
public class AdminChangeLogController {

    @Autowired
    private ChangeLogService changeLogService;

    @ApiOperation("添加 @author 卓大")
    @PostMapping("/changeLog/add")
    @PreAuthorize("@saAuth.checkPermission('changeLog:add')")
    public ResponseDTO<String> add(@RequestBody @Valid ChangeLogAddForm addForm) {
        return changeLogService.add(addForm);
    }

    @ApiOperation("更新 @author 卓大")
    @PreAuthorize("@saAuth.checkPermission('changeLog:update')")
    @PostMapping("/changeLog/update")
    public ResponseDTO<String> update(@RequestBody @Valid ChangeLogUpdateForm updateForm) {
        return changeLogService.update(updateForm);
    }

    @ApiOperation("批量删除 @author 卓大")
    @PreAuthorize("@saAuth.checkPermission('changeLog:batchDelete')")
    @PostMapping("/changeLog/batchDelete")
    public ResponseDTO<String> batchDelete(@RequestBody ValidateList<Long> idList) {
        return changeLogService.batchDelete(idList);
    }

    @ApiOperation("单个删除 @author 卓大")
    @PreAuthorize("@saAuth.checkPermission('changeLog:delete')")
    @GetMapping("/changeLog/delete/{changeLogId}")
    public ResponseDTO<String> batchDelete(@PathVariable Long changeLogId) {
        return changeLogService.delete(changeLogId);
    }
}