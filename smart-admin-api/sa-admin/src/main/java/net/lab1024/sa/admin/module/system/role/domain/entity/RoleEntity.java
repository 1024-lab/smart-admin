package net.lab1024.sa.admin.module.system.role.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 角色
 *
 * @Author 1024创新实验室: 胡克
 * @Date 2022-03-07 18:54:42
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Data
@TableName("t_role")
public class RoleEntity {
    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long roleId;

    private String roleName;

    private String remark;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
