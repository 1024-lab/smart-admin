package net.lab1024.sa.common.module.support.changelog.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.sa.common.common.domain.PageResult;
import net.lab1024.sa.common.common.domain.ResponseDTO;
import net.lab1024.sa.common.constant.SwaggerTagConst;
import net.lab1024.sa.common.module.support.changelog.domain.form.ChangeLogQueryForm;
import net.lab1024.sa.common.module.support.changelog.domain.vo.ChangeLogVO;
import net.lab1024.sa.common.module.support.changelog.service.ChangeLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
public class ChangeLogController {

    @Autowired
    private ChangeLogService changeLogService;

    @ApiOperation("分页查询 @author 卓大")
    @PostMapping("/changeLog/queryPage")
    public ResponseDTO<PageResult<ChangeLogVO>> queryPage(@RequestBody @Valid ChangeLogQueryForm queryForm) {
        return ResponseDTO.ok(changeLogService.queryPage(queryForm));
    }
}