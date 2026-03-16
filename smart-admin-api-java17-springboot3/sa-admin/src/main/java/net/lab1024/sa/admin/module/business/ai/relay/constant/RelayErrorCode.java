package net.lab1024.sa.admin.module.business.ai.relay.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.sa.base.common.code.ErrorCode;
import net.lab1024.sa.base.common.code.ErrorCodeRegister;

@Getter
@AllArgsConstructor
public enum RelayErrorCode implements ErrorCode {

    INVALID_API_KEY(50001, "无效的API Key"),
    API_KEY_EXPIRED(50002, "API Key已过期"),
    API_KEY_DISABLED(50003, "API Key已禁用"),
    ACCOUNT_DISABLED(50004, "账户已冻结"),
    INSUFFICIENT_QUOTA(50005, "额度不足"),
    MODEL_NOT_FOUND(50006, "模型不存在或已禁用"),
    NO_AVAILABLE_CHANNEL(50007, "无可用渠道"),
    UPSTREAM_ERROR(50008, "上游服务异常"),
    BILLING_ERROR(50009, "计费异常"),
    CHANNEL_NOT_FOUND(50010, "渠道不存在"),
    TOKEN_NAME_DUPLICATE(50011, "Token名称重复"),
    CHANNEL_NAME_DUPLICATE(50012, "渠道名称重复"),
    MODEL_NAME_DUPLICATE(50013, "模型名称重复"),
    ACCOUNT_NOT_FOUND(50014, "用户账户不存在"),
    TOKEN_NOT_FOUND(50015, "Token不存在"),
    CHANNEL_GROUP_NOT_FOUND(50016, "渠道分组不存在");

    static {
        ErrorCodeRegister.registerErrorCode(RelayErrorCode.class, 50001, 51000);
    }

    private final int code;
    private final String msg;
    private final String level;

    RelayErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
        this.level = LEVEL_USER;
    }
}
