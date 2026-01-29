package net.lab1024.sa.admin.module.business.knowledge.domain.form;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import net.lab1024.sa.base.common.json.deserializer.DictDataDeserializer;

/**
 * 知识库 新建表单
 *
 * @Author Mission
 * @Date 2026-01-28 10:24:07
 * @Copyright Mission
 */

@Data
public class AiKnowledgeAddForm {

    @Schema(description = "知识库名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "知识库名称 不能为空")
    private String name;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "权限 private-私有 public-公开", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "权限 private-私有 public-公开 不能为空")
    @JsonDeserialize(using = DictDataDeserializer.class)
    private String permission;

    @Schema(description = "向量模型", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "向量模型 不能为空")
    private Long embeddingModel;

    @Schema(description = "向量模型")
    @JsonDeserialize(using = DictDataDeserializer.class)
    private Integer enableRerank;

    @Schema(description = "reRank模型 当启动rerank时使用")
    private String rerankModel;

}