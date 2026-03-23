package net.lab1024.sa.admin.module.business.ai.relay.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RelayChannelVO {

    @Schema(description = "渠道ID")
    private Long channelId;

    @Schema(description = "渠道名称")
    private String channelName;

    @Schema(description = "供应商类型")
    private String providerType;

    @Schema(description = "上游Base URL")
    private String baseUrl;

    @Schema(description = "API Key掩码")
    private String apiKeyMask;

    @Schema(description = "组织ID")
    private String organization;

    @Schema(description = "优先级")
    private Integer priority;

    @Schema(description = "权重")
    private Integer weight;

    @Schema(description = "超时时间(ms)")
    private Integer timeoutMs;

    @Schema(description = "最大重试次数")
    private Integer maxRetries;

    @Schema(description = "是否禁用")
    private Integer disabledFlag;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
