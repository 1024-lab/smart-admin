package net.lab1024.sa.admin.module.system.department.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 部门 更新表单
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-01-12 20:37:48
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Data
public class DepartmentUpdateForm extends DepartmentAddForm {

    @ApiModelProperty("部门id")
    @NotNull(message = "部门id不能为空")
    private Long departmentId;

}
