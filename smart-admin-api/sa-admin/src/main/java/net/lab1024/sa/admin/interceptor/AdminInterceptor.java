package net.lab1024.sa.admin.interceptor;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.exception.SaTokenException;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.strategy.SaStrategy;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.system.login.domain.RequestEmployee;
import net.lab1024.sa.admin.module.system.login.service.LoginService;
import net.lab1024.sa.base.common.annoation.NoNeedLogin;
import net.lab1024.sa.base.common.code.SystemErrorCode;
import net.lab1024.sa.base.common.code.UserErrorCode;
import net.lab1024.sa.base.common.constant.StringConst;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.domain.SystemEnvironment;
import net.lab1024.sa.base.common.enumeration.SystemEnvironmentEnum;
import net.lab1024.sa.base.common.enumeration.UserTypeEnum;
import net.lab1024.sa.base.common.util.SmartRequestUtil;
import net.lab1024.sa.base.common.util.SmartResponseUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * admin 拦截器
 *
 * @Author 1024创新实验室-主任:卓大
 * @Date 2023/7/26 20:20:33
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>，Since 2012
 */

@Component
@Slf4j
public class AdminInterceptor implements HandlerInterceptor {

    @Resource
    private LoginService loginService;

    @Resource
    private SystemEnvironment systemEnvironment;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // OPTIONS请求直接return
        if (HttpMethod.OPTIONS.toString().equals(request.getMethod())) {
            response.setStatus(HttpStatus.NO_CONTENT.value());
            return false;
        }

        boolean isHandler = handler instanceof HandlerMethod;
        if (!isHandler) {
            return true;
        }

        try {
            // --------------- 第一步： 根据token 获取用户 ---------------

            String tokenValue = StpUtil.getTokenValue();
            boolean debugNumberTokenFlag = isDevDebugNumberToken(tokenValue);

            String loginId = null;
            if (debugNumberTokenFlag) {
                //开发、测试环境，且为数字的话，则表明为 调试临时用户，即需要调用 sa-token switch
                loginId = UserTypeEnum.ADMIN_EMPLOYEE.getValue() + StringConst.COLON + tokenValue;
                StpUtil.switchTo(loginId);
            } else {
                loginId = (String) StpUtil.getLoginIdByToken(tokenValue);
            }

            RequestEmployee requestEmployee = loginService.getLoginEmployee(loginId, request);

            // --------------- 第二步： 校验 登录 ---------------

            Method method = ((HandlerMethod) handler).getMethod();
            NoNeedLogin noNeedLogin = ((HandlerMethod) handler).getMethodAnnotation(NoNeedLogin.class);
            if (noNeedLogin != null) {
                checkActiveTimeout(requestEmployee,debugNumberTokenFlag);
                return true;
            }

            if (requestEmployee == null) {
                SmartResponseUtil.write(response, ResponseDTO.error(UserErrorCode.LOGIN_STATE_INVALID));
                return false;
            }

            // 检测token 活跃频率
            checkActiveTimeout(requestEmployee,debugNumberTokenFlag);


            // --------------- 第三步： 校验 权限 ---------------

            SmartRequestUtil.setRequestUser(requestEmployee);
            if (SaStrategy.instance.isAnnotationPresent.apply(method, SaIgnore.class)) {
                return true;
            }

            // 如果是超级管理员的话，不需要校验权限
            if(requestEmployee.getAdministratorFlag()){
               return true;
            }

            SaStrategy.instance.checkMethodAnnotation.accept(method);

        } catch (SaTokenException e) {
            /*
             * sa-token 异常状态码
             * 具体请看： https://sa-token.cc/doc.html#/fun/exception-code
             */
            int code = e.getCode();
            if (code == 11041 || code == 11051) {
                SmartResponseUtil.write(response, ResponseDTO.error(UserErrorCode.NO_PERMISSION));
            } else if (code == 11016) {
                SmartResponseUtil.write(response, ResponseDTO.error(UserErrorCode.LOGIN_ACTIVE_TIMEOUT));
            } else if (code >= 11011 && code <= 11015) {
                SmartResponseUtil.write(response, ResponseDTO.error(UserErrorCode.LOGIN_STATE_INVALID));
            } else {
                SmartResponseUtil.write(response, ResponseDTO.error(UserErrorCode.PARAM_ERROR));
            }
            return false;
        } catch (Throwable e) {
            SmartResponseUtil.write(response, ResponseDTO.error(SystemErrorCode.SYSTEM_ERROR));
            log.error(e.getMessage(), e);
            return false;
        }

        // 通过验证
        return true;
    }


    /**
     * 检测：token 最低活跃频率（单位：秒），如果 token 超过此时间没有访问系统就会被冻结
     */
    private void checkActiveTimeout(RequestEmployee requestEmployee, boolean debugNumberTokenFlag) {

        // 对于开发环境的 数字 debug token ，不需要检测活跃有效期
        if (debugNumberTokenFlag) {
            return;
        }

        // 用户不在线，也不用检测
        if (requestEmployee == null) {
            return;
        }

        StpUtil.checkActiveTimeout();
        StpUtil.updateLastActiveToNow();
    }


    /**
     * 是否为开发使用的 debug token
     *
     * @param token
     * @return
     */
    private boolean isDevDebugNumberToken(String token) {
        if (!StrUtil.isNumeric(token)) {
            return false;
        }
        return systemEnvironment.getCurrentEnvironment() == SystemEnvironmentEnum.DEV
                || systemEnvironment.getCurrentEnvironment() == SystemEnvironmentEnum.TEST;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 清除上下文
        SmartRequestUtil.remove();
        // 开发环境，关闭 sa token 的临时切换用户
        if (systemEnvironment.getCurrentEnvironment() == SystemEnvironmentEnum.DEV) {
            StpUtil.endSwitch();
        }
    }


}