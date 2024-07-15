package net.lab1024.sa.admin.module.system.support;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import net.lab1024.sa.base.common.controller.SupportBaseController;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartRequestUtil;
import net.lab1024.sa.base.constant.SwaggerTagConst;
import net.lab1024.sa.base.module.support.loginlog.LoginLogService;
import net.lab1024.sa.base.module.support.loginlog.domain.LoginLogQueryForm;
import net.lab1024.sa.base.module.support.loginlog.domain.LoginLogVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 登录日志
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022/07/22 19:46:23
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@RestController
@Tag(name = SwaggerTagConst.Support.LOGIN_LOG)
public class AdminLoginLogController extends SupportBaseController {

    @Resource
    private LoginLogService loginLogService;

    @Operation(summary = "分页查询 @author 卓大")
    @PostMapping("/loginLog/page/query")
    @SaCheckPermission("support:loginLog:query")
    public ResponseDTO<PageResult<LoginLogVO>> queryByPage(@RequestBody LoginLogQueryForm queryForm) {
        return loginLogService.queryByPage(queryForm);
    }

    @Operation(summary = "分页查询当前登录人信息 @author 善逸")
    @PostMapping("/loginLog/page/query/login")
    public ResponseDTO<PageResult<LoginLogVO>> queryByPageLogin(@RequestBody LoginLogQueryForm queryForm) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        queryForm.setUserId(requestUser.getUserId());
        queryForm.setUserType(requestUser.getUserType().getValue());
        return loginLogService.queryByPage(queryForm);
    }


}
