package net.lab1024.smartadmin.module.system.datascope.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * [  ]
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2018 1024lab.netInc. All rights reserved.
 * @date 2019/4/27 0027 下午 16:51
 * @since JDK1.8
 */
@Data
public class DataScopeBatchSetDTO {

    @ApiModelProperty("数据范围类型")
    @NotNull(message = "数据范围类型不能为空")
    private Integer dataScopeType;

    @ApiModelProperty("可见范围")
    @NotNull(message = "可见范围不能为空")
    private Integer viewType;
}
