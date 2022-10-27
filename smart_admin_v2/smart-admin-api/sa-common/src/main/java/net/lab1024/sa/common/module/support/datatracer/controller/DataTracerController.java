package net.lab1024.sa.common.module.support.datatracer.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.sa.common.common.controller.SupportBaseController;
import net.lab1024.sa.common.common.domain.PageResult;
import net.lab1024.sa.common.common.domain.ResponseDTO;
import net.lab1024.sa.common.constant.SwaggerTagConst;
import net.lab1024.sa.common.module.support.datatracer.domain.form.DataTracerQueryForm;
import net.lab1024.sa.common.module.support.datatracer.domain.vo.DataTracerVO;
import net.lab1024.sa.common.module.support.datatracer.service.DataTracerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 数据变动记录
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-07-23 19:38:52
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Api(tags = {SwaggerTagConst.Support.DATA_TRACER})
@RestController
public class DataTracerController extends SupportBaseController {

    @Autowired
    private DataTracerService dataTracerService;

    @ApiOperation("分页查询业务操作日志 - @author 卓大")
    @PostMapping("/dataTracer/query")
    public ResponseDTO<PageResult<DataTracerVO>> query(@Valid @RequestBody DataTracerQueryForm queryForm) {
        return dataTracerService.query(queryForm);
    }
}
