package net.lab1024.sa.admin.module.business.oa.enterprise.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 企业员工信息
 *
 * @Author 1024创新实验室: 罗伊
 * @Date 2022/7/28 20:37:15
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
public class EnterpriseEmployeeVO {

    private Long enterpriseEmployeeId;

    @Schema(description = "企业ID")
    private Long enterpriseId;

    @Schema(description = "企业名称")
    private String enterpriseName;

    @Schema(description = "员工")
    private Long employeeId;

    @Schema(description = "登录账号")
    private String loginName;

    @Schema(description = "员工名称")
    private String actualName;

    @Schema(description = "手机号码")
    private String phone;

    @Schema(description = "部门id")
    private Long departmentId;

    @Schema(description = "是否被禁用")
    private Boolean disabledFlag;

    @Schema(description = "部门名称")
    private String departmentName;

}
