package net.lab1024.sa.admin.module.business.ai.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * AI Chat文件表单
 *
 * @Author Mission
 * @Date 2025-12-21
 * @Copyright Mission
 */
@Data
@Schema(description = "AI Chat文件")
public class AiChatFileForm {

    @Schema(description = "文件类型", requiredMode = Schema.RequiredMode.REQUIRED,
            allowableValues = {"document", "image", "audio", "video", "custom"})
    @NotBlank(message = "文件类型不能为空")
    private String type;

    @Schema(description = "传输方法", requiredMode = Schema.RequiredMode.REQUIRED,
            allowableValues = {"remote_url", "local_file"})
    @NotBlank(message = "传输方法不能为空")
    private String transferMethod;

    @Schema(description = "文件URL（remote_url时必填）")
    private String url;

    @Schema(description = "上传文件ID（local_file时必填）")
    private String uploadFileId;
}
