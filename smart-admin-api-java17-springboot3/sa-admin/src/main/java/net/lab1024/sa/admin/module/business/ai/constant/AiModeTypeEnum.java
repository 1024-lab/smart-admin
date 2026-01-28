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
public enum AiModeTypeEnum {

    CHAT("chat", "文本"),
    EMBEDDING("embedding", "向量模型");

    private final String value;
    private final String desc;
}
