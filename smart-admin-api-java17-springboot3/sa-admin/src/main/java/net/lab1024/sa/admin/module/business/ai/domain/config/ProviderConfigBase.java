package net.lab1024.sa.admin.module.business.ai.domain.config;

import lombok.Data;

/**
 * 供应商配置基类
 *
 * @Author Mission
 * @Date 2025-12-24
 * @Copyright Mission
 */
@Data
public abstract class ProviderConfigBase {

    /**
     * 验证配置是否有效
     *
     * @throws net.lab1024.sa.base.common.exception.BusinessException 配置无效时抛出异常
     */
    public abstract void validate();

    /**
     * 获取供应商类型
     */
    public abstract String getProviderType();
}
