package net.lab1024.sa.admin.module.business.ai.model.config;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.ai.model.domain.entity.AiModelConfigEntity;
import net.lab1024.sa.admin.module.business.ai.model.service.AiModelConfigService;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Slf4j
@Component
public class DynamicModelFactory {



    /**
     * 统一使用 OpenAiChatModel，通过 baseUrl 实现多厂商兼容
     */
    @Resource
    private AiModelConfigService aiModelConfigService;

    public ChatModel createChatModel(AiModelConfigEntity config) {

        log.info("Creating NEW ChatModel instance. Provider: {}, Model: {}, BaseUrl: {}", config.getProvider(),
                config.getModelName(), config.getBaseUrl());
//        // 1. 验证参数
        checkBasic(config);
//
//        // 2. 构建 OpenAiApi (核心通讯对象)

        return OpenAiChatModel.builder()
                .baseUrl(config.getBaseUrl())
                .apiKey(config.getApiKey())
                .modelName(config.getModelName())
                .maxTokens(config.getMaxTokens())
                .build();

    }


    /**
     * Embedding 同理
     */
    public EmbeddingModel createEmbeddingModel(AiModelConfigEntity config) {
        log.info("Creating NEW EmbeddingModel instance. Provider: {}, Model: {}, BaseUrl: {}", config.getProvider(),
                config.getModelName(), config.getBaseUrl());

        checkBasic(config);

        return OpenAiEmbeddingModel.builder()
                .baseUrl(config.getBaseUrl())
                .apiKey(config.getApiKey())
                .modelName(config.getModelName())
                .build();
    }


    public ChatModel getChatModelByModelConfigId(Long modelConfigId){
        AiModelConfigEntity config = aiModelConfigService.getAiModelConfigByModelConfigId(modelConfigId);
        Assert.isTrue("chat".equals(config.getModelType()), "modelType must be chat");
        return createChatModel(config);
    }

    public EmbeddingModel getEmbeddingModelByModelConfigId(Long modelConfigId){
        AiModelConfigEntity config = aiModelConfigService.getAiModelConfigByModelConfigId(modelConfigId);
        Assert.isTrue("embedding".equals(config.getModelType()), "modelType must be embedding");
        return createEmbeddingModel(config);
    }




    private static void checkBasic(AiModelConfigEntity config) {
        Assert.hasText(config.getBaseUrl(), "baseUrl must not be empty");
        Assert.hasText(config.getApiKey(), "apiKey must not be empty");
        Assert.hasText(config.getModelName(), "modelName must not be empty");
    }

}

