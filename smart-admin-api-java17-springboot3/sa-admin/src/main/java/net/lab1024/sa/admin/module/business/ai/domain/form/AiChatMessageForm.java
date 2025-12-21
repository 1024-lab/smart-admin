package net.lab1024.sa.admin.module.business.ai.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * AI Chat消息请求表单
 *
 * @Author Mission
 * @Date 2025-12-21
 * @Copyright Mission
 */
@Data
@Schema(description = "AI Chat消息请求")
public class AiChatMessageForm {

    @Schema(description = "应用ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "应用ID不能为空")
    private String appId;

    @Schema(description = "用户消息内容", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "消息内容不能为空")
    private String query;

    @Schema(description = "输入变量")
    private Map<String, Object> inputs;

    @Schema(description = "响应模式", requiredMode = Schema.RequiredMode.REQUIRED,
            allowableValues = {"streaming", "blocking"})
    @NotBlank(message = "响应模式不能为空")
    private String responseMode;

    @Schema(description = "会话ID（可选，用于继续对话）")
    private String conversationId;

    @Schema(description = "用户标识")
    private String user;

    @Schema(description = "文件列表")
    @Valid
    private List<AiChatFileForm> files;
}
