package net.lab1024.sa.admin.module.business.ai.domain.config;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.base.common.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * 供应商配置工厂
 *
 * @Author Mission
 * @Date 2025-12-24
 * @Copyright Mission
 */
@Slf4j
@Component
public class ProviderConfigFactory {

    /**
     * 解析供应商配置
     */
    public ProviderConfigBase parseProviderConfig(String providerType, String configJson) {
        if (StringUtils.isBlank(configJson)) {
            return createDefaultConfig(providerType);
        }

        try {
            ProviderConfigBase config = switch (providerType) {
                case "dify" -> JSON.parseObject(configJson, DifyProviderConfig.class);
                case "openai" -> JSON.parseObject(configJson, OpenAiProviderConfig.class);
                // case "azure_openai" -> JSON.parseObject(configJson, AzureOpenAiProviderConfig.class);
                default -> throw new BusinessException("不支持的供应商类型: " + providerType);
            };

            config.validate();
            return config;

        } catch (Exception e) {
            log.error("解析供应商配置失败, providerType={}, configJson={}", providerType, configJson, e);
            throw new BusinessException("供应商配置格式错误: " + e.getMessage());
        }
    }

    /**
     * 解析 Dify 应用配置
     */
    public DifyAppConfig parseDifyAppConfig(String appConfigJson) {
        if (StringUtils.isBlank(appConfigJson)) {
            throw new BusinessException("Dify 应用缺少 app_config_json 配置");
        }

        try {
            DifyAppConfig config = JSON.parseObject(appConfigJson, DifyAppConfig.class);
            config.validate();
            return config;
        } catch (Exception e) {
            log.error("解析 Dify 应用配置失败, appConfigJson={}", appConfigJson, e);
            throw new BusinessException("Dify 应用配置格式错误: " + e.getMessage());
        }
    }

    /**
     * 创建默认配置
     */
    private ProviderConfigBase createDefaultConfig(String providerType) {
        return switch (providerType) {
            case "dify" -> new DifyProviderConfig();
            case "openai" -> new OpenAiProviderConfig();
            default -> throw new BusinessException("不支持的供应商类型: " + providerType);
        };
    }
}
