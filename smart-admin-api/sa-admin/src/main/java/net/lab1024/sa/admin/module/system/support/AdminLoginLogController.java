package net.lab1024.sa.admin.module.system.support;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.sa.common.common.controller.SupportBaseController;
import net.lab1024.sa.common.common.domain.PageResult;
import net.lab1024.sa.common.common.domain.ResponseDTO;
import net.lab1024.sa.common.constant.SwaggerTagConst;
import net.lab1024.sa.common.module.support.loginlog.LoginLogService;
import net.lab1024.sa.common.module.support.loginlog.domain.LoginLogQueryForm;
import net.lab1024.sa.common.module.support.loginlog.domain.LoginLogVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录日志
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022/07/22 19:46:23
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@RestController
@Api(tags = {SwaggerTagConst.Support.LOGIN_LOG})
public class AdminLoginLogController extends SupportBaseController {

    @Autowired
    private LoginLogService loginLogService;

    @ApiOperation(value = "分页查询 @author 卓大")
    @PreAuthorize("@saAuth.checkPermission('loginLog:query')")
    @PostMapping("/loginLog/page/query")
    public ResponseDTO<PageResult<LoginLogVO>> queryByPage(@RequestBody LoginLogQueryForm queryForm) {
        return loginLogService.queryByPage(queryForm);
    }


}
