package net.lab1024.sa.admin.module.business.knowledge.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 知识库 实体类
 *
 * @Author Mission
 * @Date 2026-01-28 10:24:07
 * @Copyright Mission
 */

@Data
@TableName("t_ai_knowledge")
public class AiKnowledgeEntity {

    /**
     * 知识库id
     */
    @TableId
    private String id;

    /**
     * 知识库名称
     */
    private String name;

    /**
     * knowledge-base
     */
    private String type;

    /**
     * 描述
     */
    private String description;

    /**
     * 权限 private-私有 public-公开
     */
    private String permission;

    /**
     * 向量模型
     */
    private String embeddingModel;

    /**
     * 向量模型
     */
    private Integer enableRerank;

    /**
     * reRank模型 当启动rerank时使用
     */
    private String rerankModel;

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
    private LocalDateTime updateAt;

    /**
     * 删除标志
     */
    private Integer deletedFlag;

}
