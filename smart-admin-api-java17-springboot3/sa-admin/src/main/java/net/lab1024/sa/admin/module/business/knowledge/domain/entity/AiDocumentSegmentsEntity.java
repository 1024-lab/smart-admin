package net.lab1024.sa.admin.module.business.knowledge.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 文档分块表 实体类
 *
 * @Author Mission
 * @Date 2026-01-28 20:45:56
 * @Copyright Mission
 */

@Data
@TableName("t_ai_document_segments")
public class AiDocumentSegmentsEntity {

    /**
     * id
     */
    @TableId
    private Long id;

    /**
     * 知识库id
     */
    private Long knowledgeId;

    /**
     * 文档名称
     */
    private Long documentId;

    /**
     * 知识类型: DOCUMENT-文档, QA-问答
     */
    private String type;

    /**
     * 文档字数
     */
    private Integer wordCount;

    /**
     * 排序
     */
    private Integer position;

    /**
     * 分块内容
     */
    private String content;

    /**
     * 回答，QA类型才有
     */
    private String answer;

    /**
     * 状态 pending-待处理 processing处理中 completed成功 fail失败
     */
    private String status;

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
