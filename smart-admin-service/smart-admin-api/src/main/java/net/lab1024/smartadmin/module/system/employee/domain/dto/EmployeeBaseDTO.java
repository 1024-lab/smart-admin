package net.lab1024.smartadmin.module.system.employee.domain.dto;

import net.lab1024.smartadmin.util.SmartVerificationUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 添加员工
 *
 * @author lidoudou
 * @date 2017年12月19日下午2:06:31
 */
@Data
public class EmployeeBaseDTO {

    @ApiModelProperty("姓名")
    @NotNull(message = "姓名不能为空")
    private String actualName;

    @ApiModelProperty("登录名")
    @NotNull(message = "姓名不能为空")
    private String loginName;

    @ApiModelProperty("别名")
    private String nickName;

    @ApiModelProperty("部门id")
    @NotNull(message = "部门id不能为空")
    private Long departmentId;

    @ApiModelProperty("是否启用")
    @NotNull(message = "是否被禁用不能为空")
    private Integer isDisabled;

    @ApiModelProperty("手机号")
    @NotNull(message = "手机号不能为空")
    @Pattern(regexp = SmartVerificationUtil.PHONE_REGEXP, message = "手机号格式不正确")
    private String phone;

    @ApiModelProperty("身份证(可选)")
    private String idCard;

    @ApiModelProperty("生日(可选)")
    private String birthday;

    @ApiModelProperty("邮箱")
    private String email;

}
