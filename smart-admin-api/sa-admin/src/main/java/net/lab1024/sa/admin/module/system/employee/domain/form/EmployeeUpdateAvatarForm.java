package net.lab1024.sa.admin.module.system.employee.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.base.common.util.SmartVerificationUtil;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 修改登录人头像
 *
 * @Author 1024创新实验室: 善逸
 * @Date 2024年6月30日00:26:35
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
public class EmployeeUpdateAvatarForm {

    @Schema(hidden = true)
    private Long employeeId;

    @Schema(description = "头像")
    @NotBlank(message = "头像不能为空哦")
    private String avatar;
}
