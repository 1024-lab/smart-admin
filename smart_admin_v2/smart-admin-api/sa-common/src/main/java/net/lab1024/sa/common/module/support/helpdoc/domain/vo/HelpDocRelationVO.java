package net.lab1024.sa.common.module.support.helpdoc.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 帮助文档 关联项目
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-08-20 23:11:42
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Data
public class HelpDocRelationVO {

    @ApiModelProperty("关联名称")
    private String relationName;

    @ApiModelProperty("关联id")
    private Long relationId;
}
