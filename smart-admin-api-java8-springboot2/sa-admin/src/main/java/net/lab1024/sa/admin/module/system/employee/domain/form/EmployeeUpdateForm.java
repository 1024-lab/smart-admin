package net.lab1024.sa.admin.module.system.employee.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 更新员工
 *
 * @Author 1024创新实验室: 开云
 * @Date 2021-12-20 21:06:49
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
public class EmployeeUpdateForm extends EmployeeAddForm {

    @Schema(description = "员工id")
    @NotNull(message = "员工id不能为空")
    private Long employeeId;
}
