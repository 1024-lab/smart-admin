package net.lab1024.sa.base.module.support.securityprotect.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 三级等保相关配置
 *
 * @Author 1024创新实验室-创始人兼主任:卓大
 * @Date 2024/7/30
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a> ，Since 2012
 */

@Data
public class Level3ProtectConfigForm {

    @Schema(description = "连续登录失败次数则锁定")
    @NotNull(message = "连续登录失败次数则锁定 不能为空")
    private Integer loginFailMaxTimes;

    @Schema(description = "连续登录失败锁定时间（单位：分钟）")
    @NotNull(message = "连续登录失败锁定时间（单位：分钟） 不能为空")
    private Integer loginFailLockMinutes;

    @Schema(description = "最低活跃时间（单位：分钟）")
    @NotNull(message = "最低活跃时间（单位：分钟） 不能为空")
    private Integer loginActiveTimeoutMinutes;

    @Schema(description = "开启双因子登录")
    @NotNull(message = "开启双因子登录 不能为空")
    private Boolean twoFactorLoginEnabled;

    @Schema(description = "密码复杂度 是否开启，默认：开启")
    @NotNull(message = "密码复杂度 是否开启 不能为空")
    private Boolean passwordComplexityEnabled;

    @Schema(description = "定期修改密码时间间隔（默认：月）")
    @NotNull(message = "定期修改密码时间间隔（默认：月） 不能为空")
    private Integer regularChangePasswordMonths;

    @Schema(description = "定期修改密码不允许重复次数，默认：3次以内密码不能相同（默认：次）")
    @NotNull(message = "定期修改密码不允许重复次数 不能为空")
    private Integer regularChangePasswordNotAllowRepeatTimes;

    @Schema(description = "文件检测，默认：不开启")
    @NotNull(message = "文件检测 是否开启 不能为空")
    private Boolean fileDetectFlag;

    @Schema(description = "文件大小限制，单位 mb ，(默认：50 mb)")
    @NotNull(message = "文件大小限制  不能为空")
    private Long maxUploadFileSizeMb;


}
