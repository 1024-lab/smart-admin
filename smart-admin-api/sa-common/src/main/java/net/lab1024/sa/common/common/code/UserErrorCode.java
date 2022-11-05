package net.lab1024.sa.common.common.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户级别的错误码（用户引起的错误返回码，可以不用关注）
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2021/09/21 22:12:27
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Getter
@AllArgsConstructor
public enum UserErrorCode implements ErrorCode {

    PARAM_ERROR(30001, "参数错误"),

    DATA_NOT_EXIST(30002, "左翻右翻，数据竟然找不到了~"),

    ALREADY_EXIST(30003, "数据已存在了呀~"),

    REPEAT_SUBMIT(30004, "亲~您操作的太快了，请稍等下再操作~"),

    NO_PERMISSION(30005, "对不起，您无法访问此资源哦~"),

    DEVELOPING(30006, "系統正在紧急开发中，敬请期待~"),

    LOGIN_STATE_INVALID(30007, "您还未登录或登录失效，请重新登录！"),

    USER_STATUS_ERROR(30008, "用户状态异常"),

    FORM_REPEAT_SUBMIT(30009, "请勿重复提交");

    private final int code;

    private final String msg;

    private final String level;

    UserErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
        this.level = LEVEL_USER;
    }
}
