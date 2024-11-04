package net.lab1024.sa.base.common.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 系统错误状态码（此类返回码应该高度重视）
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2021/10/24 20:09
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Getter
@AllArgsConstructor
public enum SystemErrorCode implements ErrorCode {

    /**
     * 系统错误
     */
    SYSTEM_ERROR(10001, "系统似乎出现了点小问题"),

    ;

    private final int code;

    private final String msg;

    private final String level;

    SystemErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
        this.level = LEVEL_SYSTEM;
    }

}

