package net.lab1024.sa.admin.module.business.ai.provider;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import net.lab1024.sa.base.common.code.UserErrorCode;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * AI供应商工厂
 *
 * @Author Mission
 * @Date 2025-12-21
 * @Copyright Mission
 */
@Component
public class AiProviderFactory {

    private final Map<String, AiChatProvider> providerMap = new ConcurrentHashMap<>();

    @Resource
    private List<AiChatProvider> providers;

    @PostConstruct
    public void init() {
        for (AiChatProvider provider : providers) {
            providerMap.put(provider.getProviderType(), provider);
        }
    }

    public AiChatProvider getProvider(String providerType) {
        AiChatProvider provider = providerMap.get(providerType);
        if (provider == null) {
            throw ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST,
                "不支持的供应商类型: " + providerType).exception();
        }
        return provider;
    }
}
