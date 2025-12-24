package net.lab1024.sa.admin.module.business.ai.domain.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.lab1024.sa.base.common.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;

/**
 * OpenAI 供应商配置
 *
 * @Author Mission
 * @Date 2025-12-24
 * @Copyright Mission
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OpenAiProviderConfig extends ProviderConfigBase {

    /**
     * 模型名称,如 gpt-4, gpt-3.5-turbo
     */
    private String model = "gpt-4";

    /**
     * 温度参数 0.0-2.0
     */
    private Double temperature = 0.7;

    /**
     * 最大 token 数
     */
    private Integer maxTokens = 2000;

    @Override
    public void validate() {
        if (StringUtils.isBlank(model)) {
            throw new BusinessException("OpenAI 配置缺少 model 参数");
        }
        if (temperature == null || temperature < 0 || temperature > 2) {
            throw new BusinessException("OpenAI temperature 必须在 0-2 之间");
        }
    }

    @Override
    public String getProviderType() {
        return "openai";
    }
}
