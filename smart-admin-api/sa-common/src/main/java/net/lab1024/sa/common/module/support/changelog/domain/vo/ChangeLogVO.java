package net.lab1024.sa.common.module.support.changelog.domain.vo;

import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;
import net.lab1024.sa.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.sa.common.module.support.changelog.constant.ChangeLogTypeEnum;

/**
 * 系统更新日志 列表VO
 *
 * @Author 卓大
 * @Date 2022-09-26 14:53:50
 * @Copyright 1024创新实验室
 */

@Data
public class ChangeLogVO {

    private Long changeLogId;

    @ApiModelProperty(value = "版本")
    private String version;

    @ApiModelPropertyEnum(value = ChangeLogTypeEnum.class, desc = "更新类型:[1:特大版本功能更新;2:功能更新;3:bug修复]")
    private Integer type;

    @ApiModelProperty(value = "发布人")
    private String publishAuthor;

    @ApiModelProperty(value = "发布日期")
    private LocalDate publicDate;

    @ApiModelProperty(value = "更新内容")
    private String content;

    @ApiModelProperty(value = "跳转链接")
    private String link;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

}