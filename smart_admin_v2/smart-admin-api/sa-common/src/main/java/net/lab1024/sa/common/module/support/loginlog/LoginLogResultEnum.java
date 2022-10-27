package net.lab1024.sa.common.module.support.loginlog;

import net.lab1024.sa.common.common.enumeration.BaseEnum;

/**
 * 登录类型
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022/07/22 19:46:23
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
public enum LoginLogResultEnum implements BaseEnum {
    
    LOGIN_SUCCESS(0, "登录成功"),
    LOGIN_FAIL(1, "登录失败"),
    LOGIN_OUT(2, "退出登录");

    private Integer type;
    private String desc;

    LoginLogResultEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    @Override
    public Integer getValue() {
        return type;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}
