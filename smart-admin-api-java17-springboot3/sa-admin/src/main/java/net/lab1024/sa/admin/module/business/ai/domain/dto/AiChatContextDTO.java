package net.lab1024.sa.admin.module.business.ai.domain.dto;

import lombok.Data;
import net.lab1024.sa.admin.module.business.ai.domain.form.AiChatFileForm;
import net.lab1024.sa.admin.module.business.ai.domain.config.ProviderConfigBase;

import java.util.List;
import java.util.Map;

/**
 * AI Chat上下文传输对象
 *
 * @Author Mission
 * @Date 2025-12-21
 * @Copyright Mission
 */
@Data
public class AiChatContextDTO {

    private String baseUrl;
    private String apiKey;
    private String query;
    private Map<String, Object> inputs;
    private String conversationId;
    private String user;
    private List<AiChatFileForm> files;

    /**
     * 平台配置对象
     */
    private ProviderConfigBase providerConfig;

    /**
     * 应用级配置 JSON
     */
    private String appConfigJson;
}
