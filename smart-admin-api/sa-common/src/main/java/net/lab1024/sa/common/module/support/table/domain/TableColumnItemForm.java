package net.lab1024.sa.common.module.support.table.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 自定义表格列
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-08-12 22:52:21
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Data
public class TableColumnItemForm {

    @NotEmpty(message = "列不能为空")
    @ApiModelProperty("字段")
    private String columnKey;

    @ApiModelProperty("宽度")
    private Integer width;

    @NotNull(message = "显示不能为空")
    @ApiModelProperty("是否显示")
    private Boolean showFlag;

    @NotNull(message = "排序不能为空")
    @ApiModelProperty("排序")
    private Integer sort;


}
