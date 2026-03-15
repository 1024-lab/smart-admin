package net.lab1024.sa.admin.module.business.ai.relay.provider;

import lombok.Data;

/**
 * 用量解析结果
 */
@Data
public class UsageResult {

    private int promptTokens;

    private int completionTokens;

    private int totalTokens;

    public static UsageResult of(int promptTokens, int completionTokens) {
        UsageResult result = new UsageResult();
        result.setPromptTokens(promptTokens);
        result.setCompletionTokens(completionTokens);
        result.setTotalTokens(promptTokens + completionTokens);
        return result;
    }
}
