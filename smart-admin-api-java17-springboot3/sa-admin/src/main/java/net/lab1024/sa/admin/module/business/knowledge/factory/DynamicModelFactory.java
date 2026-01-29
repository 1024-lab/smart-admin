package net.lab1024.sa.admin.module.business.knowledge.factory;

import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.ai.domain.entity.AiModelConfigEntity;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.MetadataMode;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.retry.RetryUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * 动态模型工厂
 * 用于根据配置动态创建 ChatModel 和 EmbeddingModel
 *
 * @Author Mission
 * @Date 2026-01-29
 * @Copyright Mission
 */
@Slf4j
@Service
public class DynamicModelFactory {

    /**
     * 统一使用 OpenAiChatModel，通过 baseUrl 实现多厂商兼容
     */
    public ChatModel createChatModel(AiModelConfigEntity config) {
        log.info("Creating NEW ChatModel instance. Provider: {}, Model: {}, BaseUrl: {}",
                config.getProvider(), config.getModelName(), config.getBaseUrl());

        // 1. 验证参数
        checkBasic(config);

        // 2. 构建 OpenAiApi (核心通讯对象)
        OpenAiApi.Builder apiBuilder = OpenAiApi.builder()
                .apiKey(config.getApiKey())
                .baseUrl(config.getBaseUrl());

        if (StringUtils.hasText(config.getCompletionsPath())) {
            apiBuilder.completionsPath(config.getCompletionsPath());
        }
        OpenAiApi openAiApi = apiBuilder.build();

        // 3. 构建运行时选项 (设置默认的模型名称，如 "deepseek-chat" 或 "gpt-4")
        OpenAiChatOptions.Builder optionsBuilder = OpenAiChatOptions.builder()
                .model(config.getModelName());

        if (config.getTemperature() != null) {
            optionsBuilder.temperature(config.getTemperature().doubleValue());
        }
        if (config.getMaxTokens() != null) {
            optionsBuilder.maxTokens(config.getMaxTokens());
        }

        OpenAiChatOptions openAiChatOptions = optionsBuilder.build();

        // 4. 返回统一的 OpenAiChatModel
        return OpenAiChatModel.builder()
                .openAiApi(openAiApi)
                .defaultOptions(openAiChatOptions)
                .build();
    }

    /**
     * 验证基础配置参数
     */
    private static void checkBasic(AiModelConfigEntity config) {
        Assert.hasText(config.getBaseUrl(), "baseUrl must not be empty");
        Assert.hasText(config.getApiKey(), "apiKey must not be empty");
        Assert.hasText(config.getModelName(), "modelName must not be empty");
    }

    /**
     * 创建 Embedding 模型
     */
    public EmbeddingModel createEmbeddingModel(AiModelConfigEntity config) {
        log.info("Creating NEW EmbeddingModel instance. Provider: {}, Model: {}, BaseUrl: {}",
                config.getProvider(), config.getModelName(), config.getBaseUrl());

        checkBasic(config);

        OpenAiApi.Builder apiBuilder = OpenAiApi.builder()
                .apiKey(config.getApiKey())
                .baseUrl(config.getBaseUrl());

        if (StringUtils.hasText(config.getEmbeddingsPath())) {
            apiBuilder.embeddingsPath(config.getEmbeddingsPath());
        }

        OpenAiApi openAiApi = apiBuilder.build();

        return new OpenAiEmbeddingModel(
                openAiApi,
                MetadataMode.EMBED,
                OpenAiEmbeddingOptions.builder().model(config.getModelName()).build(),
                RetryUtils.DEFAULT_RETRY_TEMPLATE
        );
    }
}
