package net.lab1024.sa.admin.module.system.department.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 部门 添加表单
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-01-12 20:37:48
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
public class DepartmentAddForm {

    @Schema(description = "部门名称")
    @Length(min = 1, max = 50, message = "请输入正确的部门名称(1-50个字符)")
    @NotNull(message = "请输入正确的部门名称(1-50个字符)")
    private String departmentName;

    @Schema(description = "排序")
    @NotNull(message = "排序值")
    private Integer sort;

    @Schema(description = "部门负责人id")
    private Long managerId;

    @Schema(description = "上级部门id (可选)")
    private Long parentId;

}
