package net.lab1024.sa.admin.module.business.oa.invoice.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * OA发票信息新建
 *
 * @Author 1024创新实验室: 善逸
 * @Date 2022-06-23 19:32:59
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
public class InvoiceAddForm {

    @Schema(description = "开票抬头")
    @NotBlank(message = "开票抬头不能为空")
    @Length(max = 200, message = "开票抬头最多200字符")
    private String invoiceHeads;

    @Schema(description = "纳税人识别号")
    @NotBlank(message = "纳税人识别号不能为空")
    @Length(max = 200, message = "纳税人识别号最多200字符")
    private String taxpayerIdentificationNumber;

    @Schema(description = "银行账户")
    @NotBlank(message = "银行账户不能为空")
    @Length(max = 200, message = "银行账户最多200字符")
    private String accountNumber;

    @Schema(description = "开户行")
    @NotBlank(message = "开户行不能为空")
    @Length(max = 200, message = "开户行最多200字符")
    private String bankName;

    @Schema(description = "启用状态")
    @NotNull(message = "启用状态不能为空")
    private Boolean disabledFlag;

    @Schema(description = "备注")
    @Length(max = 500, message = "备注最多500字符")
    private String remark;

    @Schema(description = "企业")
    @NotNull(message = "企业不能为空")
    private Long enterpriseId;

    @Schema(description = "创建人", hidden = true)
    private Long createUserId;

    @Schema(description = "创建人名称", hidden = true)
    private String createUserName;
}
