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
public class DataScopeViewTypeVO {

    @ApiModelProperty("可见范围")
    private Integer viewType;

    @ApiModelProperty("可见范围名称")
    private String viewTypeName;

    @ApiModelProperty("级别,用于表示范围大小")
    private Integer viewTypeLevel;
}
