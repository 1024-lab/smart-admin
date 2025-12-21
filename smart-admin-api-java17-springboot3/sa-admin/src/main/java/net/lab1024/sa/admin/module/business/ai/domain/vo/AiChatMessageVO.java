package net.lab1024.sa.admin.module.business.ai.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Map;

/**
 * AI Chat消息响应VO
 *
 * @Author Mission
 * @Date 2025-12-21
 * @Copyright Mission
 */
@Data
@Schema(description = "AI Chat消息响应")
public class AiChatMessageVO {

    @Schema(description = "事件类型")
    private String event;

    @Schema(description = "消息ID")
    private String messageId;

    @Schema(description = "会话ID")
    private String conversationId;

    @Schema(description = "模式")
    private String mode;

    @Schema(description = "回答内容")
    private String answer;

    @Schema(description = "创建时间戳")
    private Long createdAt;

    @Schema(description = "元数据")
    private Map<String, Object> metadata;

    @Schema(description = "错误信息")
    private String error;
}
