package net.lab1024.sa.common.common.domain;


import lombok.Data;
import net.lab1024.sa.common.common.code.ErrorCode;
import net.lab1024.sa.common.common.code.UserErrorCode;
import org.apache.commons.lang3.StringUtils;

/**
 * 请求返回对象
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2021-10-31 21:06:11
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Data
public class ResponseDTO<T> {

    public static final int OK_CODE = 0;

    public static final String OK_MSG = "success";

    private Integer code;

    private String level;

    private String msg;

    private Boolean ok;

    private T data;

    public ResponseDTO(Integer code, String level, boolean ok, String msg, T data) {
        this.code = code;
        this.level = level;
        this.ok = ok;
        this.msg = msg;
        this.data = data;
    }

    public ResponseDTO(ErrorCode errorCode, boolean ok, String msg, T data) {
        this.code = errorCode.getCode();
        this.level = errorCode.getLevel();
        this.ok = ok;
        if (StringUtils.isNotBlank(msg)) {
            this.msg = msg;
        } else {
            this.msg = errorCode.getMsg();
        }
        this.data = data;
    }

    public static <T> ResponseDTO<T> ok() {
        return new ResponseDTO<>(OK_CODE, null, true, OK_MSG, null);
    }

    public static <T> ResponseDTO<T> ok(T data) {
        return new ResponseDTO<>(OK_CODE, null, true, OK_MSG, data);
    }

    public static <T> ResponseDTO<T> okMsg(String msg) {
        return new ResponseDTO<>(OK_CODE, null, true, msg, null);
    }

    // -------------------------------------------- 最常用的 用户参数 错误码 --------------------------------------------

    public static <T> ResponseDTO<T> userErrorParam() {
        return new ResponseDTO<>(UserErrorCode.PARAM_ERROR, false, null, null);
    }


    public static <T> ResponseDTO<T> userErrorParam(String msg) {
        return new ResponseDTO<>(UserErrorCode.PARAM_ERROR, false, msg, null);
    }

    // -------------------------------------------- 错误码 --------------------------------------------

    public static <T> ResponseDTO<T> error(ErrorCode errorCode) {
        return new ResponseDTO<>(errorCode, false, null, null);
    }

    public static <T> ResponseDTO<T> error(ErrorCode errorCode, boolean ok) {
        return new ResponseDTO<>(errorCode, ok, null, null);
    }

    public static ResponseDTO error(ResponseDTO responseDTO) {
        return new ResponseDTO<>(responseDTO.getCode(), responseDTO.getLevel(), responseDTO.getOk(), responseDTO.getMsg(), responseDTO.getData());
    }

    public static <T> ResponseDTO<T> error(ErrorCode errorCode, String msg) {
        return new ResponseDTO<>(errorCode, false, msg, null);
    }

    public static <T> ResponseDTO<T> errorData(ErrorCode errorCode, T data) {
        return new ResponseDTO<>(errorCode, false, null, data);
    }


}
