package net.lab1024.sa.admin.module.system.datascope.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * 数据范围
 *
 * @Author 1024创新实验室: 罗伊
 * @Date 2020/11/28  20:59:17
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Data
@Builder
public class DataScopeDTO {

    @ApiModelProperty("数据范围类型")
    private Integer dataScopeType;

    @ApiModelProperty("数据范围名称")
    private String dataScopeTypeName;

    @ApiModelProperty("描述")
    private String dataScopeTypeDesc;

    @ApiModelProperty("顺序")
    private Integer dataScopeTypeSort;

}
