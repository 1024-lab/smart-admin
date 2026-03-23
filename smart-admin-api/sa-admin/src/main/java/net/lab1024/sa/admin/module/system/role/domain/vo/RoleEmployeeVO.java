package net.lab1024.sa.admin.module.system.role.domain.vo;

import lombok.Data;

/**
 * 角色的员工
 *
 * @Author 1024创新实验室: 罗伊
 * @Date 2022-04-08 21:53:04
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
public class RoleEmployeeVO {

    private Long roleId;

    private Long employeeId;

    private String roleName;
}
