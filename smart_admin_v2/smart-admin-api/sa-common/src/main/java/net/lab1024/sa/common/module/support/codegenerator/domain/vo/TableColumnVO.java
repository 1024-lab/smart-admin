
package net.lab1024.sa.common.module.support.codegenerator.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 列
 *
 * @Author 1024创新实验室-主任:卓大
 * @Date 2022/9/21 21:07:58
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ），2012-2022
 */

@Data
public class TableColumnVO {

    @ApiModelProperty("列名")
    private String columnName;

    @ApiModelProperty("列描述")
    private String columnComment;

    @ApiModelProperty("columnKey")
    private String columnKey;

    @ApiModelProperty("extra")
    private String extra;

    @ApiModelProperty("是否为空")
    private String isNullable;

    @ApiModelProperty("数据类型varchar")
    private String dataType;

    @ApiModelProperty("列类型varchar(50)")
    private String columnType;


}
