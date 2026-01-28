package net.lab1024.sa.admin.module.business.ai.domain.form;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;
import net.lab1024.sa.base.common.json.deserializer.DictDataDeserializer;

/**
 * 模型配置表 更新表单
 *
 * @Author Mission
 * @Date 2026-01-28 09:59:38
 * @Copyright Mission
 */

@Data
public class AiModelConfigUpdateForm {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "id 不能为空")
    private Integer id;

    @Schema(description = "厂商标识 (方便前端展示回显，实际调用主要靠 baseUrl)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "厂商标识 (方便前端展示回显，实际调用主要靠 baseUrl) 不能为空")
    @JsonDeserialize(using = DictDataDeserializer.class)
    private String provider;

    @Schema(description = "关键配置", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "关键配置 不能为空")
    private String baseUrl;

    @Schema(description = "API密钥", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "API密钥 不能为空")
    private String apiKey;

    @Schema(description = "模型名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "模型名称 不能为空")
    private String modelName;

    @Schema(description = "温度参数")
    private BigDecimal temperature;

    @Schema(description = "是否激活")
    @JsonDeserialize(using = DictDataDeserializer.class)
    private Integer isActive;

    @Schema(description = "输出响应最大令牌数")
    private Integer maxTokens;

    @Schema(description = "模型类型 (CHAT/EMBEDDING)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "模型类型 (CHAT/EMBEDDING) 不能为空")
    @JsonDeserialize(using = DictDataDeserializer.class)
    private String modelType;

    @Schema(description = "Chat模型专用。附加到 Base URL 的路径。例如OpenAi的/v1/chat/completions")
    private String completionsPath;

    @Schema(description = "嵌入模型专用。附加到 Base URL 的路径。")
    private String embeddingsPath;

}