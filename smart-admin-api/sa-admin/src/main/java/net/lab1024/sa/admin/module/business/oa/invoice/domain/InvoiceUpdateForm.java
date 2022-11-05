package net.lab1024.sa.admin.module.business.oa.invoice.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * OA发票信息编辑
 *
 * @Author 1024创新实验室: 善逸
 * @Date 2022-06-23 19:32:59
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ），2012-2022
 */
@Data
public class InvoiceUpdateForm extends InvoiceAddForm {

    @ApiModelProperty("发票信息ID")
    @NotNull(message = "发票信息ID不能为空")
    private Long invoiceId;
}
