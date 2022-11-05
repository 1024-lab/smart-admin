package net.lab1024.sa.admin.module.system.employee.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 员工更新角色
 *
 * @Author 1024创新实验室: 罗伊
 * @Date 2021-12-20 20:55:13
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Data
public class EmployeeUpdateRoleForm {

    @ApiModelProperty("员工id")
    @NotNull(message = "员工id不能为空")
    private Long employeeId;

    @ApiModelProperty("角色ids")
    @Size(max = 99, message = "角色最多99")
    private List<Long> roleIdList;

}
