package net.lab1024.sa.admin.module.business.oa.enterprise.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 企业员工
 *
 * @Author 1024创新实验室: 罗伊
 * @Date 2022/7/28 20:37:15
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
public class EnterpriseEmployeeForm {

    @Schema(description = "企业id")
    @NotNull(message = "企业id不能为空")
    private Long enterpriseId;

    @Schema(description = "员工信息id")
    @NotEmpty(message = "员工信息id不能为空")
    private List<Long> employeeIdList;
}