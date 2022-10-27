package net.lab1024.sa.common.module.support.serialnumber.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 单据序列号 生成表单
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-03-25 21:46:07
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Data
public class SerialNumberGenerateForm {

    @ApiModelProperty("单号id")
    @NotNull(message = "单号id不能为空")
    private Integer serialNumberId;

    @ApiModelProperty("生成的数量")
    @NotNull(message = "生成的数量")
    private Integer count;

}
