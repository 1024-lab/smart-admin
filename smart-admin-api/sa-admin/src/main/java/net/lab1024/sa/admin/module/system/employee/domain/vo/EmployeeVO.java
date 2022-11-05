package net.lab1024.sa.admin.module.system.employee.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.sa.common.common.enumeration.GenderEnum;
import net.lab1024.sa.common.common.swagger.ApiModelPropertyEnum;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 员工信息
 *
 * @Author 1024创新实验室: 罗伊
 * @Date 2021-12-21 23:05:56
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Data
public class EmployeeVO {

    @ApiModelProperty("主键id")
    private Long employeeId;

    @ApiModelProperty("登录账号")
    private String loginName;

    @ApiModelPropertyEnum(GenderEnum.class)
    private Integer gender;

    @ApiModelProperty("员工名称")
    private String actualName;

    @ApiModelProperty("手机号码")
    private String phone;

    @ApiModelProperty("部门id")
    private Long departmentId;

    @ApiModelProperty("是否被禁用")
    private Boolean disabledFlag;

    @ApiModelProperty("是否 超级管理员")
    private Boolean administratorFlag;

    @ApiModelProperty("部门名称")
    private String departmentName;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("角色列表")
    private List<Long> roleIdList;

    @ApiModelProperty("角色名称列表")
    private List<String> roleNameList;
}
