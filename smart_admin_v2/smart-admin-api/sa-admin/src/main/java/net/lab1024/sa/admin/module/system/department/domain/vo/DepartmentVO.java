package net.lab1024.sa.admin.module.system.department.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 部门
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-01-12 20:37:48
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Data
public class DepartmentVO {

    @ApiModelProperty("部门id")
    private Long departmentId;

    @ApiModelProperty("部门名称")
    private String name;

    @ApiModelProperty("部门负责人姓名")
    private String managerName;

    @ApiModelProperty("部门负责人id")
    private Long managerId;

    @ApiModelProperty("父级部门id")
    private Long parentId;

    @ApiModelProperty("排序")
    private Integer sort;

}
