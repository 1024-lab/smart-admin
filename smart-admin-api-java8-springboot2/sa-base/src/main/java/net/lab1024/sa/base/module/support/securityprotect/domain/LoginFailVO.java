package net.lab1024.sa.base.module.support.securityprotect.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 登录失败 列表VO
 *
 * @Author 1024创新实验室-主任-卓大
 * @Date 2023-10-17 18:02:37
 * @Copyright 1024创新实验室
 */

@Data
public class LoginFailVO {

    private Long loginFailId;


    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "用户类型")
    private Integer userType;

    @Schema(description = "登录名")
    private String loginName;

    @Schema(description = "连续登录失败次数")
    private Integer loginFailCount;

    @Schema(description = "锁定状态:1锁定，0未锁定")
    private Integer lockFlag;

    @Schema(description = "连续登录失败锁定开始时间")
    private LocalDateTime loginLockBeginTime;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

}