package net.lab1024.sa.admin.module.business.ai.domain.config;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Dify 供应商配置
 * Dify 不需要供应商级别配置,所有配置在应用级别
 *
 * @Author Mission
 * @Date 2025-12-24
 * @Copyright Mission
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DifyProviderConfig extends ProviderConfigBase {

    @Override
    public void validate() {
        // Dify 供应商级别无需配置
    }

    @Override
    public String getProviderType() {
        return "dify";
    }
}
