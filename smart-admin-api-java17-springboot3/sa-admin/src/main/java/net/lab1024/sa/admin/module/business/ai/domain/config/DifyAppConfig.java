package net.lab1024.sa.admin.module.business.ai.domain.config;

import lombok.Data;
import net.lab1024.sa.base.common.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;

/**
 * Dify 应用级配置
 *
 * @Author Mission
 * @Date 2025-12-24
 * @Copyright Mission
 */
@Data
public class DifyAppConfig {

    /**
     * Dify 应用专属 API Key
     */
    private String apiKey;

    /**
     * 验证配置
     */
    public void validate() {
        if (StringUtils.isBlank(apiKey)) {
            throw new BusinessException("Dify 应用配置缺少 apiKey");
        }
    }
}
