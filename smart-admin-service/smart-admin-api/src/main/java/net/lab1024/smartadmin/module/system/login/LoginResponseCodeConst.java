package net.lab1024.smartadmin.module.system.login;

import net.lab1024.smartadmin.common.constant.ResponseCodeConst;

/**
 * 员工常量类
 * 1001-1999
 *
 * @author lidoudou
 * @date 2017年12月19日下午19:04:52
 */
public class LoginResponseCodeConst extends ResponseCodeConst {

    public static final LoginResponseCodeConst LOGIN_ERROR = new LoginResponseCodeConst(1001, "您还未登录或登录失效，请重新登录！");

    public static final LoginResponseCodeConst NOT_HAVE_PRIVILEGES = new LoginResponseCodeConst(1002, "对不起，您没有权限哦！");

    public LoginResponseCodeConst(int code, String msg) {
        super(code, msg);
    }
}
