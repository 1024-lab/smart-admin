package net.lab1024.sa.admin.module.business.knowledge.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 文档上传表单
 *
 * @Author Mission
 * @Date 2026-01-29
 * @Copyright Mission
 */
@Data
@Schema(description = "文档上传表单")
public class AiDocumentsAddForm {

    @Schema(description = "知识库ID")
    @NotNull(message = "知识库ID不能为空")
    private Long knowledgeId;

    @Schema(description = "文档名称")
    @NotNull(message = "文档名称不能为空")
    private String name;

    @Schema(description = "文件ID")
    @NotNull(message = "文件ID不能为空")
    private String fileId;
}
