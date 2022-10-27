package net.lab1024.sa.common.module.support.helpdoc.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
public class HelpDocRelationForm {

    @ApiModelProperty("关联名称")
    @NotBlank(message = "关联名称不能为空")
    private String relationName;

    @ApiModelProperty("关联id")
    @NotNull(message = "关联id不能为空")
    private Long relationId;
}
