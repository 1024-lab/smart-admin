package net.lab1024.sa.base.module.support.changelog.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;
import net.lab1024.sa.base.common.swagger.SchemaEnum;
import net.lab1024.sa.base.module.support.changelog.constant.ChangeLogTypeEnum;

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

    @Schema(description = "版本")
    private String version;

    @SchemaEnum(value = ChangeLogTypeEnum.class, desc = "更新类型:[1:特大版本功能更新;2:功能更新;3:bug修复]")
    private Integer type;

    @Schema(description = "发布人")
    private String publishAuthor;

    @Schema(description = "发布日期")
    private LocalDate publicDate;

    @Schema(description = "更新内容")
    private String content;

    @Schema(description = "跳转链接")
    private String link;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

}