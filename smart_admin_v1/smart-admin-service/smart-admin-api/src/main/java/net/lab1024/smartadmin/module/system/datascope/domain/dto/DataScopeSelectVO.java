package net.lab1024.smartadmin.module.system.datascope.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * [  ]
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2018 1024lab.netInc. All rights reserved.
 * @date 2019/4/27 0027 下午 16:43
 * @since JDK1.8
 */
@Data
public class DataScopeSelectVO {

    @ApiModelProperty("数据范围id")
    private Integer dataScopeType;

    @ApiModelProperty("可见范围")
    private Integer viewType;
}
