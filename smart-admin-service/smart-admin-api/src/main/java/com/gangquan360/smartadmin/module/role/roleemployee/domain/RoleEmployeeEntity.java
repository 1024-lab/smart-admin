package com.gangquan360.smartadmin.module.role.roleemployee.domain;

import com.baomidou.mybatisplus.annotations.TableName;
import com.gangquan360.smartadmin.common.domain.BaseEntity;
import lombok.Data;

/**
 * [ 角色 员工关系]
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2018 1024lab.netInc. All rights reserved.
 * @date 2019/3/27 0027 下午 13:01
 * @since JDK1.8
 */
@Data
@TableName("t_role_employee")
public class RoleEmployeeEntity extends BaseEntity{

    private Long roleId;

    private Long employeeId;
}
