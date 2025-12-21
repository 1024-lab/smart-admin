package net.lab1024.sa.admin.module.business.ai.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * AI供应商类型枚举
 *
 * @Author Mission
 * @Date 2025-12-21
 * @Copyright Mission
 */
@Getter
@AllArgsConstructor
public enum AiProviderTypeEnum {

    DIFY("dify", "Dify"),
    OPENAI("openai", "OpenAI"),
    AZURE_OPENAI("azure_openai", "Azure OpenAI");

    private final String value;
    private final String desc;
}
