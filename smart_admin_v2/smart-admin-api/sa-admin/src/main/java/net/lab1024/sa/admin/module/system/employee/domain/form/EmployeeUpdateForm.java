package net.lab1024.sa.admin.module.system.employee.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 更新员工
 *
 * @Author 1024创新实验室: 开云
 * @Date 2021-12-20 21:06:49
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Data
public class EmployeeUpdateForm extends EmployeeAddForm {

    @ApiModelProperty("员工id")
    @NotNull(message = "员工id不能为空")
    private Long employeeId;
}
