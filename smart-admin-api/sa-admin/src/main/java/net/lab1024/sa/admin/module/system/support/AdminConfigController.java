package net.lab1024.sa.admin.module.system.support;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.sa.common.common.controller.SupportBaseController;
import net.lab1024.sa.common.common.domain.PageResult;
import net.lab1024.sa.common.common.domain.ResponseDTO;
import net.lab1024.sa.common.constant.SwaggerTagConst;
import net.lab1024.sa.common.module.support.config.ConfigService;
import net.lab1024.sa.common.module.support.config.domain.ConfigAddForm;
import net.lab1024.sa.common.module.support.config.domain.ConfigQueryForm;
import net.lab1024.sa.common.module.support.config.domain.ConfigUpdateForm;
import net.lab1024.sa.common.module.support.config.domain.ConfigVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 配置
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-03-14 20:46:27
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Api(tags = {SwaggerTagConst.Support.CONFIG})
@RestController
public class AdminConfigController extends SupportBaseController {

    @Autowired
    private ConfigService configService;

    @ApiOperation("分页查询系统配置 @author 卓大")
    @PreAuthorize("@saAuth.checkPermission('support:config:query')")
    @PostMapping("/config/query")
    public ResponseDTO<PageResult<ConfigVO>> querySystemConfigPage(@RequestBody @Valid ConfigQueryForm queryForm) {
        return configService.queryConfigPage(queryForm);
    }

    @ApiOperation("添加配置参数 @author 卓大")
    @PreAuthorize("@saAuth.checkPermission('support:config:add')")
    @PostMapping("/config/add")
    public ResponseDTO<String> addSystemConfig(@RequestBody @Valid ConfigAddForm configAddForm) {
        return configService.add(configAddForm);
    }

    @ApiOperation("修改配置参数 @author 卓大")
    @PreAuthorize("@saAuth.checkPermission('support:config:update')")
    @PostMapping("/config/update")
    public ResponseDTO<String> updateSystemConfig(@RequestBody @Valid ConfigUpdateForm updateForm) {
        return configService.updateSystemConfig(updateForm);
    }

}
