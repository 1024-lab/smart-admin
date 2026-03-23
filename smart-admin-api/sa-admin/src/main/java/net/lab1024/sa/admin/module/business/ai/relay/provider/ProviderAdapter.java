package net.lab1024.sa.admin.module.business.ai.relay.provider;

import net.lab1024.sa.admin.module.business.ai.relay.constant.EndpointTypeEnum;
import net.lab1024.sa.admin.module.business.ai.relay.constant.ProviderTypeEnum;
import net.lab1024.sa.admin.module.business.ai.relay.domain.entity.RelayChannelEntity;

/**
 * Provider 适配器接口 — 核心扩展点
 * <p>
 * 新增 provider 只需:
 * 1. 新建 provider/{name}/ 目录实现此接口
 * 2. 在 ProviderAdapterFactory 注册
 * 3. ProviderTypeEnum 加枚举值
 */
public interface ProviderAdapter {

    /**
     * 当前适配器对应的供应商类型
     */
    ProviderTypeEnum getProviderType();

    /**
     * 是否支持指定端点类型
     */
    boolean supportsEndpoint(EndpointTypeEnum endpointType);

    /**
     * 构建上游请求
     *
     * @param channel        渠道实体
     * @param decryptedApiKey 解密后的 API Key
     * @param requestBody    原始请求体
     * @param endpointType   端点类型
     * @return 上游请求
     */
    UpstreamRequest buildUpstreamRequest(RelayChannelEntity channel, String decryptedApiKey,
                                          String requestBody, EndpointTypeEnum endpointType);

    /**
     * 从非流式响应体解析 usage
     */
    UsageResult parseUsage(String responseBody);

    /**
     * 从流式最后一个 data chunk 解析 usage
     */
    UsageResult parseStreamUsage(String lastDataChunk);

    /**
     * 当上游未返回 usage 时，根据请求和聚合文本估算
     */
    UsageResult estimateUsage(String requestBody, String aggregatedResponseText);

    /**
     * 解析上游错误信息
     */
    String parseErrorMessage(int statusCode, String responseBody);
}
