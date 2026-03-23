package net.lab1024.sa.admin.module.business.ai.relay.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RelayChannelAddForm {

    @Schema(description = "渠道名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "渠道名称不能为空")
    private String channelName;

    @Schema(description = "供应商类型", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "供应商类型不能为空")
    private String providerType;

    @Schema(description = "上游Base URL", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Base URL不能为空")
    private String baseUrl;

    @Schema(description = "上游API Key(明文，存储时加密)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "API Key不能为空")
    private String apiKey;

    @Schema(description = "组织ID")
    private String organization;

    @Schema(description = "优先级(数字越小越优先)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "优先级不能为空")
    private Integer priority;

    @Schema(description = "权重(同优先级内加权随机)")
    private Integer weight;

    @Schema(description = "超时时间(ms)")
    private Integer timeoutMs;

    @Schema(description = "最大重试次数")
    private Integer maxRetries;

    @Schema(description = "备注")
    private String remark;
}
