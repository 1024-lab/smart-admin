package net.lab1024.sa.admin.module.business.ai.channel.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class AiChannelVO {

    @Schema(description = "渠道ID")
    private Long id;

    @Schema(description = "渠道名称")
    private String channelName;

    @Schema(description = "供应商类型")
    private String providerType;

    @Schema(description = "上游base URL")
    private String baseUrl;

    @Schema(description = "优先级")
    private Integer priority;

    @Schema(description = "权重")
    private Integer weight;

    @Schema(description = "状态 0=禁用 1=启用")
    private Integer status;

    @Schema(description = "绑定的模型名称列表")
    private List<String> modelNames;

    @Schema(description = "创建人姓名")
    private String createByName;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
