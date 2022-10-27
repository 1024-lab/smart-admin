package net.lab1024.sa.admin.module.business.oa.bank.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.sa.common.common.domain.PageParam;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

/**
 * OA办公-OA银行信息查询
 *
 * @Author 1024创新实验室:善逸
 * @Date 2022/6/23 21:59:22
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ），2012-2022
 */
@Data
public class BankQueryForm extends PageParam {

    @ApiModelProperty("企业ID")
    private Long enterpriseId;

    @ApiModelProperty("关键字")
    @Length(max = 200, message = "关键字最多200字符")
    private String keywords;

    @ApiModelProperty("开始时间")
    private LocalDate startTime;

    @ApiModelProperty("结束时间")
    private LocalDate endTime;

    @ApiModelProperty("禁用状态")
    private Boolean disabledFlag;

    @ApiModelProperty(value = "删除状态", hidden = true)
    private Boolean deletedFlag;
}
