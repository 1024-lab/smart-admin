package net.lab1024.sa.admin.module.business.oa.invoice.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * OA发票信息
 *
 * @Author 1024创新实验室: 善逸
 * @Date 2022-06-23 19:32:59
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ），2012-2022
 */
@Data
public class InvoiceVO {

    @ApiModelProperty("发票信息ID")
    private Long invoiceId;

    @ApiModelProperty("开票抬头")
    private String invoiceHeads;

    @ApiModelProperty("纳税人识别号")
    private String taxpayerIdentificationNumber;

    @ApiModelProperty("银行账户")
    private String accountNumber;

    @ApiModelProperty("开户行")
    private String bankName;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("企业")
    private Long enterpriseId;

    @ApiModelProperty("企业名称")
    private String enterpriseName;

    @ApiModelProperty("禁用状态")
    private Boolean disabledFlag;

    @ApiModelProperty("创建人ID")
    private Long createUserId;

    @ApiModelProperty("创建人名称")
    private String createUserName;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
}
