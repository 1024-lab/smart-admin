package net.lab1024.sa.admin.module.business.oa.bank.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * OA办公-OA银行信息
 *
 * @Author 1024创新实验室:善逸
 * @Date 2022/6/23 21:59:22
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ），2012-2022
 */
@Data
public class BankVO {

    @ApiModelProperty("银行信息ID")
    private Long bankId;

    @ApiModelProperty("开户银行")
    private String bankName;

    @ApiModelProperty("账户名称")
    private String accountName;

    @ApiModelProperty("账号")
    private String accountNumber;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("是否对公")
    private Boolean businessFlag;

    @ApiModelProperty("企业ID")
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
