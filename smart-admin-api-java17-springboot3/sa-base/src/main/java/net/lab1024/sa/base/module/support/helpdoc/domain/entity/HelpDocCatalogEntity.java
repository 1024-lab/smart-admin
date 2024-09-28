package net.lab1024.sa.base.module.support.helpdoc.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 帮助文档的 类型
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-08-20 23:11:42
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@TableName("t_help_doc_catalog")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HelpDocCatalogEntity {

    @TableId(type = IdType.AUTO)
    private Long helpDocCatalogId;

    /**
     * 名称
     */
    private String name;

    /**
     * 父id
     */
    private Long parentId;

    /**
     * 排序
     */
    @TableField("`sort`")
    private Integer sort;


    private LocalDateTime updateTime;

    private LocalDateTime createTime;
}
