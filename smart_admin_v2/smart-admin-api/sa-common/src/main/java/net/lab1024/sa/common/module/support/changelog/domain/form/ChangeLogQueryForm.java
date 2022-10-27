package net.lab1024.sa.common.module.support.changelog.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.sa.common.common.domain.PageParam;
import net.lab1024.sa.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.sa.common.common.validator.enumeration.CheckEnum;
import net.lab1024.sa.common.module.support.changelog.constant.ChangeLogTypeEnum;

import java.time.LocalDate;

/**
 * 系统更新日志 查询
 *
 * @Author 卓大
 * @Date 2022-09-26 14:53:50
 * @Copyright 1024创新实验室
 */

@Data
public class ChangeLogQueryForm extends PageParam{

    @ApiModelPropertyEnum(value = ChangeLogTypeEnum.class, desc = "更新类型:[1:特大版本功能更新;2:功能更新;3:bug修复]")
    @CheckEnum(value = ChangeLogTypeEnum.class, message = "更新类型:[1:特大版本功能更新;2:功能更新;3:bug修复] 错误")
    private Integer type;

    @ApiModelProperty(value = "关键字")
    private String keyword;

    @ApiModelProperty(value = "发布日期")
    private LocalDate publicDateBegin;

    @ApiModelProperty(value = "发布日期")
    private LocalDate publicDateEnd;

    @ApiModelProperty(value = "创建时间")
    private LocalDate createTime;

    @ApiModelProperty(value = "跳转链接")
    private String link;

}