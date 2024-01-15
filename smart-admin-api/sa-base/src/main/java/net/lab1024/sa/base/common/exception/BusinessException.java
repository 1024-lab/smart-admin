package net.lab1024.sa.base.common.exception;

import net.lab1024.sa.base.common.code.ErrorCode;

/**
 * 业务逻辑异常,全局异常拦截后统一返回ResponseCodeConst.SYSTEM_ERROR
 *
 * @Author 1024创新实验室: 罗伊
 * @Date 2020/8/25 21:57
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
public class BusinessException extends RuntimeException {

    public BusinessException() {
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMsg());
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    public BusinessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
