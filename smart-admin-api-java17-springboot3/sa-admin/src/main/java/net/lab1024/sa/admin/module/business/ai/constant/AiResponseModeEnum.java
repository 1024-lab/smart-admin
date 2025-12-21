package net.lab1024.sa.admin.module.business.ai.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * AI响应模式枚举
 *
 * @Author Mission
 * @Date 2025-12-21
 * @Copyright Mission
 */
@Getter
@AllArgsConstructor
public enum AiResponseModeEnum {

    STREAMING("streaming", "流式响应"),
    BLOCKING("blocking", "阻塞响应");

    private final String value;
    private final String desc;
}
