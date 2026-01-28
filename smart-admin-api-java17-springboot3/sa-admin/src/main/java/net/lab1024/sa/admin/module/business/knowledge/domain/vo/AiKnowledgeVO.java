package net.lab1024.sa.admin.module.business.knowledge.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 知识库 列表VO
 *
 * @Author Mission
 * @Date 2026-01-28 10:24:07
 * @Copyright Mission
 */

@Data
public class AiKnowledgeVO {


    @Schema(description = "知识库id")
    private String id;

    @Schema(description = "知识库名称")
    private String name;

    @Schema(description = "knowledge-base")
    private String type;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "权限 private-私有 public-公开")
    private String permission;

    @Schema(description = "向量模型")
    private String embeddingModel;

    @Schema(description = "向量模型")
    private Integer enableRerank;

    @Schema(description = "reRank模型 当启动rerank时使用")
    private String rerankModel;

    @Schema(description = "创建人姓名")
    private String createByName;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新人姓名")
    private String updateByName;

    @Schema(description = "更新时间")
    private LocalDateTime updateAt;

}
