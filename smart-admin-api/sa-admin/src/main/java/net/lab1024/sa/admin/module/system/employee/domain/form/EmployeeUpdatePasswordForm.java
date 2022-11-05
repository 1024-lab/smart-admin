package net.lab1024.sa.admin.module.system.employee.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.sa.common.common.util.SmartVerificationUtil;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 修改密码所需参数
 *
 * @Author 1024创新实验室: 开云
 * @Date 2021-12-20 21:06:49
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Data
public class EmployeeUpdatePasswordForm {

    @ApiModelProperty(hidden = true)
    private Long employeeId;

    @ApiModelProperty("原密码")
    @NotBlank(message = "原密码不能为空哦")
    @Pattern(regexp = SmartVerificationUtil.PWD_REGEXP, message = "原密码请输入6-15位(数字|大小写字母|小数点)")
    private String oldPassword;

    @ApiModelProperty("新密码")
    @NotBlank(message = "新密码不能为空哦")
    @Pattern(regexp = SmartVerificationUtil.PWD_REGEXP, message = "新密码请输入6-15位(数字|大小写字母|小数点)")
    private String newPassword;
}
