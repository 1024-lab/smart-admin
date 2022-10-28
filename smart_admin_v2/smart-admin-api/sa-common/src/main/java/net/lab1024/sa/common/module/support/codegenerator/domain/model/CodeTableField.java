package net.lab1024.sa.common.module.support.codegenerator.domain.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 代码生成 列表表格 模型
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-06-30 22:15:38
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */

@Data
public class CodeTableField {

    @NotBlank(message = "6、列表 列名 不能为空")
    @ApiModelProperty("列名")
    private String columnName;

    @NotBlank(message = "6、列表 字段名词 不能为空")
    @ApiModelProperty("字段名词")
    private String label;

    @NotBlank(message = "6、列表  字段命名 不能为空")
    @ApiModelProperty("字段命名")
    private String fieldName;

    @NotNull(message = "6、列表 列表显示 不能为空")
    @ApiModelProperty("列表显示")
    private Boolean showFlag;

    @ApiModelProperty("宽度")
    private Integer width;

    @NotNull(message = "6、列表 自动省略标识 不能为空")
    @ApiModelProperty("自动省略标识")
    private Boolean ellipsisFlag;


}
