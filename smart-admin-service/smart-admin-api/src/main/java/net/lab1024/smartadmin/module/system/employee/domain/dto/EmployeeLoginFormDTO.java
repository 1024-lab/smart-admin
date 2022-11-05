package net.lab1024.smartadmin.module.system.employee.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 登录
 *
 * @author lidoudou
 * @date 2017年12月19日上午11:49:46
 */
@Data
public class EmployeeLoginFormDTO {

    @NotNull(message = "登录名不能为空")
    @ApiModelProperty(example = "sa")
    private String loginName;

    @NotNull(message = "密码不能为空")
    @ApiModelProperty(example = "123456")
    private String loginPwd;

    @ApiModelProperty(value = "验证码uuid")
    private String codeUuid;

    @ApiModelProperty(value = "验证码")
    private String code;

}
