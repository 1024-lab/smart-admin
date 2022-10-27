package net.lab1024.sa.admin.module.business.oa.enterprise.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 企业员工信息
 *
 * @Author 1024创新实验室: 罗伊
 * @Date 2022/7/28 20:37:15
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ），2012-2022
 */
@Data
public class EnterpriseEmployeeVO {

    private Long enterpriseEmployeeId;

    @ApiModelProperty("企业ID")
    private Long enterpriseId;

    @ApiModelProperty("企业名称")
    private String enterpriseName;

    @ApiModelProperty("员工")
    private Long employeeId;

    @ApiModelProperty("登录账号")
    private String loginName;

    @ApiModelProperty("员工名称")
    private String actualName;

    @ApiModelProperty("手机号码")
    private String phone;

    @ApiModelProperty("部门id")
    private Long departmentId;

    @ApiModelProperty("是否被禁用")
    private Boolean disabledFlag;

    @ApiModelProperty("部门名称")
    private String departmentName;

}
