package net.lab1024.sa.base.module.support.changelog.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 系统更新日志
 *
 * @Author 卓大
 * @Date 2022-09-26 14:53:50
 * @Copyright 1024创新实验室
 */

@Data
@TableName("t_change_log")
public class ChangeLogEntity {

    /**
     * 更新日志id
     */
    @TableId(type = IdType.AUTO)
    private Long changeLogId;

    /**
     * 版本
     */
    private String version;

    /**
     * 更新类型:[1:特大版本功能更新;2:功能更新;3:bug修复]
     */
    private Integer type;

    /**
     * 发布人
     */
    private String publishAuthor;

    /**
     * 发布日期
     */
    private LocalDate publicDate;

    /**
     * 更新内容
     */
    private String content;

    /**
     * 跳转链接
     */
    private String link;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}