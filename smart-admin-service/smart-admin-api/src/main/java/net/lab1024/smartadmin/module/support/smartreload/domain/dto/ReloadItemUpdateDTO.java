package net.lab1024.smartadmin.module.support.smartreload.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
/**
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2019 1024lab.netInc. All rights reserved.
 * @date
 * @since JDK1.8
 */
@Data
public class ReloadItemUpdateDTO {

    @ApiModelProperty("标签")
    @NotBlank(message = "标签不能为空")
    private String tag;

    @ApiModelProperty("状态标识")
    @NotBlank(message = "状态标识不能为空")
    private String identification;

    @ApiModelProperty("reload时传入的参数，可以为空")
    private String args;
}
