package net.lab1024.sa.admin.module.system.employee.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 员工更新部门
 *
 * @Author 1024创新实验室: 开云
 * @Date 2021-12-20 21:06:49
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
public class EmployeeBatchUpdateDepartmentForm {

    @Schema(description = "员工id")
    @NotEmpty(message = "员工id不能为空")
    @Size(max = 99, message = "一次最多调整99个员工")
    private List<Long> employeeIdList;

    @Schema(description = "部门ID")
    @NotNull(message = "部门ID不能为空")
    private Long departmentId;
}
