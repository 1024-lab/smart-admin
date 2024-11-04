package net.lab1024.sa.admin.module.business.oa.bank.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * OA办公-银行信息新建
 *
 * @Author 1024创新实验室:善逸
 * @Date 2022/6/23 21:59:22
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
public class BankCreateForm {

    @Schema(description = "开户银行")
    @NotBlank(message = "开户银行不能为空")
    @Length(max = 200, message = "开户银行最多200字符")
    private String bankName;

    @Schema(description = "账户名称")
    @NotBlank(message = "账户名称不能为空")
    @Length(max = 200, message = "账户名称最多200字符")
    private String accountName;

    @Schema(description = "账号")
    @NotBlank(message = "账号不能为空")
    @Length(max = 200, message = "账号最多200字符")
    private String accountNumber;

    @Schema(description = "备注")
    @Length(max = 500, message = "备注最多500字符")
    private String remark;

    @Schema(description = "是否对公")
    @NotNull(message = "是否对公不能为空")
    private Boolean businessFlag;

    @Schema(description = "企业")
    @NotNull(message = "企业不能为空")
    private Long enterpriseId;

    @Schema(description = "禁用状态")
    @NotNull(message = "禁用状态不能为空")
    private Boolean disabledFlag;

    @Schema(hidden = true)
    private Long createUserId;

    @Schema(hidden = true)
    private String createUserName;
}
