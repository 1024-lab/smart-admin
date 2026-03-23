package net.lab1024.sa.admin.module.business.ai.relay.provider;

import net.lab1024.sa.admin.module.business.ai.relay.constant.ProviderTypeEnum;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provider 适配器工厂
 */
@Component
public class ProviderAdapterFactory {

    @Resource
    private List<ProviderAdapter> adapters;

    private final Map<String, ProviderAdapter> adapterMap = new HashMap<>();

    @PostConstruct
    public void init() {
        for (ProviderAdapter adapter : adapters) {
            adapterMap.put(adapter.getProviderType().getValue(), adapter);
        }
    }

    public ProviderAdapter getAdapter(String providerType) {
        ProviderAdapter adapter = adapterMap.get(providerType);
        if (adapter == null) {
            throw new IllegalArgumentException("不支持的供应商类型: " + providerType);
        }
        return adapter;
    }
}
