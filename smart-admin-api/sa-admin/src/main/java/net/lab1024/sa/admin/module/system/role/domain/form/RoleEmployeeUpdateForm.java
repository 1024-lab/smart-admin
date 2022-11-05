package net.lab1024.sa.admin.module.system.role.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 角色的员工更新
 *
 * @Author 1024创新实验室: 善逸
 * @Date 2022-04-08 21:53:04
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Data
public class RoleEmployeeUpdateForm {

    @ApiModelProperty("角色id")
    @NotNull(message = "角色id不能为空")
    protected Long roleId;

    @ApiModelProperty(value = "员工id集合")
    @NotEmpty(message = "员工id不能为空")
    protected List<Long> employeeIdList;

}
