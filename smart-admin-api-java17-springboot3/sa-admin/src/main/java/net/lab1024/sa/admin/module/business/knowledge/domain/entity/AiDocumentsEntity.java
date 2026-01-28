package net.lab1024.sa.admin.module.business.knowledge.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 文档列表 实体类
 *
 * @Author Mission
 * @Date 2026-01-28 20:36:21
 * @Copyright Mission
 */

@Data
@TableName("t_ai_documents")
public class AiDocumentsEntity {

    /**
     * id
     */
    @TableId
    private String id;

    /**
     * 知识库id
     */
    private Long knowledgeId;

    /**
     * 文档名称
     */
    private String name;

    /**
     * 文档字数
     */
    private Integer wordCount;

    /**
     * 状态 pending-待处理 success-成功 fail-失败
     */
    private String status;

    /**
     * 文件id
     */
    private String fileId;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建人姓名
     */
    private String createByName;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新人姓名
     */
    private String updateByName;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}
