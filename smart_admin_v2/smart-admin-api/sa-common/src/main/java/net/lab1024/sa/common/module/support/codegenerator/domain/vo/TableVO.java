
package net.lab1024.sa.common.module.support.codegenerator.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 表信息
 *
 * @Author 1024创新实验室-主任:卓大
 * @Date 2022/9/21 18:07:58
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ），2012-2022
 */

@Data
public class TableVO {

    @ApiModelProperty("表名")
    private String tableName;

    @ApiModelProperty("表备注")
    private String tableComment;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty("配置时间")
    private LocalDateTime configTime;

}
