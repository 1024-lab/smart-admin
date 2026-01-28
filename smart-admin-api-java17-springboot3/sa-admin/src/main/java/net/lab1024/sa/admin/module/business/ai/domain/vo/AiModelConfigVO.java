package net.lab1024.sa.admin.module.business.ai.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 模型配置表 列表VO
 *
 * @Author Mission
 * @Date 2026-01-28 09:59:38
 * @Copyright Mission
 */

@Data
public class AiModelConfigVO {


    @Schema(description = "id")
    private Integer id;

    @Schema(description = "厂商标识 (方便前端展示回显，实际调用主要靠 baseUrl)")
    private String provider;

    @Schema(description = "关键配置")
    private String baseUrl;

    @Schema(description = "API密钥")
    private String apiKey;

    @Schema(description = "模型名称")
    private String modelName;

    @Schema(description = "温度参数")
    private BigDecimal temperature;

    @Schema(description = "是否激活")
    private Integer isActive;

    @Schema(description = "输出响应最大令牌数")
    private Integer maxTokens;

    @Schema(description = "模型类型 (CHAT/EMBEDDING)")
    private String modelType;

    @Schema(description = "Chat模型专用。附加到 Base URL 的路径。例如OpenAi的/v1/chat/completions")
    private String completionsPath;

    @Schema(description = "嵌入模型专用。附加到 Base URL 的路径。")
    private String embeddingsPath;

    @Schema(description = "创建人姓名")
    private String createByName;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新人姓名")
    private String updateByName;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

}
