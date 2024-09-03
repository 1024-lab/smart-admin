package net.lab1024.sa.admin.module.system.employee.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.base.common.enumeration.GenderEnum;
import net.lab1024.sa.base.common.swagger.SchemaEnum;
import net.lab1024.sa.base.common.validator.enumeration.CheckEnum;
import org.hibernate.validator.constraints.Length;
import net.lab1024.sa.base.common.util.SmartVerificationUtil;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * 添加员工
 *
 * @Author 1024创新实验室: 开云
 * @Date 2021-12-20 21:06:49
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
public class EmployeeAddForm {

    @Schema(description = "姓名")
    @NotNull(message = "姓名不能为空")
    @Length(max = 30, message = "姓名最多30字符")
    private String actualName;

    @Schema(description = "登录账号")
    @NotNull(message = "登录账号不能为空")
    @Length(max = 30, message = "登录账号最多30字符")
    private String loginName;

    @SchemaEnum(GenderEnum.class)
    @CheckEnum(value = GenderEnum.class, message = "性别错误")
    private Integer gender;

    @Schema(description = "部门id")
    @NotNull(message = "部门id不能为空")
    private Long departmentId;

    @Schema(description = "是否启用")
    @NotNull(message = "是否被禁用不能为空")
    private Boolean disabledFlag;

    @Schema(description = "手机号")
    @NotNull(message = "手机号不能为空")
    @Pattern(regexp = SmartVerificationUtil.PHONE_REGEXP, message = "手机号格式不正确")
    private String phone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "角色列表")
    private List<Long> roleIdList;

    @Schema(description = "备注")
    @Length(max = 30, message = "备注最多200字符")
    private String remark;

    @Schema(description = "职务级别ID")
    private Long positionId;

}
