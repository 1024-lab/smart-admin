package net.lab1024.sa.admin.module.business.oa.invoice.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * OA发票信息
 *
 * @Author 1024创新实验室: 善逸
 * @Date 2022-06-23 19:32:59
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
public class InvoiceVO {

    @Schema(description = "发票信息ID")
    private Long invoiceId;

    @Schema(description = "开票抬头")
    private String invoiceHeads;

    @Schema(description = "纳税人识别号")
    private String taxpayerIdentificationNumber;

    @Schema(description = "银行账户")
    private String accountNumber;

    @Schema(description = "开户行")
    private String bankName;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "企业")
    private Long enterpriseId;

    @Schema(description = "企业名称")
    private String enterpriseName;

    @Schema(description = "禁用状态")
    private Boolean disabledFlag;

    @Schema(description = "创建人ID")
    private Long createUserId;

    @Schema(description = "创建人名称")
    private String createUserName;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
