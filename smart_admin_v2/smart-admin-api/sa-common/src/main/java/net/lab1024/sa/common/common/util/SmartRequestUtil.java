package net.lab1024.sa.common.common.util;

import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.common.common.domain.RequestUser;

/**
 * 请求用户  工具类
 *
 * @Author 1024创新实验室: 罗伊
 * @Date 2022-05-30 21:22:12
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Slf4j
public class SmartRequestUtil {

    private static final ThreadLocal<RequestUser> requestThreadLocal = new ThreadLocal<>();

    public static void setRequestUser(RequestUser requestUser) {
        requestThreadLocal.set(requestUser);
    }

    public static RequestUser getRequestUser() {
        return requestThreadLocal.get();
    }

    public static Long getRequestUserId() {
        RequestUser requestUser = getRequestUser();
        return null == requestUser ? null : requestUser.getUserId();
    }

    public static void remove() {
        requestThreadLocal.remove();
    }


}
