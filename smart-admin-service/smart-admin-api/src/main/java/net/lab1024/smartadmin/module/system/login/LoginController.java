package net.lab1024.smartadmin.module.system.login;

import net.lab1024.smartadmin.common.anno.NoNeedLogin;
import net.lab1024.smartadmin.common.anno.NoValidPrivilege;
import net.lab1024.smartadmin.common.anno.OperateLog;
import net.lab1024.smartadmin.common.domain.ResponseDTO;
import net.lab1024.smartadmin.constant.SwaggerTagConst;
import net.lab1024.smartadmin.module.system.employee.domain.dto.EmployeeLoginFormDTO;
import net.lab1024.smartadmin.module.system.login.domain.KaptchaVO;
import net.lab1024.smartadmin.module.system.login.domain.LoginDetailVO;
import net.lab1024.smartadmin.module.system.login.domain.RequestTokenBO;
import net.lab1024.smartadmin.util.SmartRequestTokenUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 后台登录
 *
 * @author lidoudou
 * @date 2017年12月19日上午11:46:04
 */
@RestController
@Api(tags = {SwaggerTagConst.Admin.MANAGER_USER_LOGIN})
@OperateLog
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/session/login")
    @ApiOperation(value = "登录", notes = "登录")
    @NoNeedLogin
    public ResponseDTO<LoginDetailVO> login(@Valid @RequestBody EmployeeLoginFormDTO loginForm, HttpServletRequest request) {
        return loginService.login(loginForm, request);
    }


    @GetMapping("/session/get")
    @ApiOperation(value = "获取session", notes = "获取session")
    @NoValidPrivilege
    public ResponseDTO<LoginDetailVO> getSession() {
        RequestTokenBO requestUser = SmartRequestTokenUtil.getRequestUser();
        return ResponseDTO.succData(loginService.getSession(requestUser));
    }

    @GetMapping("/session/logOut")
    @ApiOperation(value = "退出登陆", notes = "退出登陆")
    @NoValidPrivilege
    public ResponseDTO<Boolean> logOut() {
        RequestTokenBO requestToken = SmartRequestTokenUtil.getRequestUser();
        if (null == requestToken) {
            return ResponseDTO.wrap(LoginResponseCodeConst.LOGIN_ERROR);
        }
        return loginService.logoutByToken(requestToken);
    }

    @GetMapping("/session/verificationCode")
    @ApiOperation(value = "获取验证码", notes = "获取验证码")
    @NoNeedLogin
    public ResponseDTO<KaptchaVO> verificationCode() {
        return loginService.verificationCode();
    }

}
