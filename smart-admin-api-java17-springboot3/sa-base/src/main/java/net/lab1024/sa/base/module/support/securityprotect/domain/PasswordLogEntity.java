package net.lab1024.sa.base.module.support.securityprotect.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author yandy
 * @description:
 * @date 2024/7/15 1:39 下午
 */
@Data
@TableName("t_password_log")
public class PasswordLogEntity {

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    private Integer userType;

    private Long userId;

    private String oldPassword;

    private String newPassword;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}