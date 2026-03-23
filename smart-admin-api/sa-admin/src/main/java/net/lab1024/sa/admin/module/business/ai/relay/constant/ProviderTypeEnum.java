package net.lab1024.sa.admin.module.business.ai.relay.constant;

import net.lab1024.sa.base.common.enumeration.BaseEnum;

public enum ProviderTypeEnum implements BaseEnum {

    OPENAI("OPENAI", "OpenAI"),
    AZURE("AZURE", "Azure OpenAI"),
    CLAUDE("CLAUDE", "Anthropic Claude"),
    GEMINI("GEMINI", "Google Gemini"),
    DEEPSEEK("DEEPSEEK", "DeepSeek");

    private final String value;
    private final String desc;

    ProviderTypeEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}
