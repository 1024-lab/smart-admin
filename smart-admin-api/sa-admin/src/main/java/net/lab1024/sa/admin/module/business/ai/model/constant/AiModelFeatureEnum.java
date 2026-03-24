package net.lab1024.sa.admin.module.business.ai.model.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * AI 模型能力标签枚举（同一端点的附加能力，存储为逗号分隔字符串）
 *
 * @Author Mission
 */
@Getter
@AllArgsConstructor
public enum AiModelFeatureEnum {

    VISION("VISION", "视觉输入"),
    FUNCTION_CALL("FUNCTION_CALL", "函数调用"),
    STREAMING("STREAMING", "流式输出");

    private final String code;
    private final String desc;
}
