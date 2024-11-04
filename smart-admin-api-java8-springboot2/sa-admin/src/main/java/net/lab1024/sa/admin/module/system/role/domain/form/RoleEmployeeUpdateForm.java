package net.lab1024.sa.admin.module.system.role.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * 角色的员工更新
 *
 * @Author 1024创新实验室: 善逸
 * @Date 2022-04-08 21:53:04
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
public class RoleEmployeeUpdateForm {

    @Schema(description = "角色id")
    @NotNull(message = "角色id不能为空")
    protected Long roleId;

    @Schema(description = "员工id集合")
    @NotEmpty(message = "员工id不能为空")
    protected Set<Long> employeeIdList;

}
