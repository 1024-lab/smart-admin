package net.lab1024.sa.admin.module.business.ai.relay.service.relay;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.ai.relay.constant.EndpointTypeEnum;
import net.lab1024.sa.admin.module.business.ai.relay.context.RelayRequestContext;
import net.lab1024.sa.admin.module.business.ai.relay.domain.entity.RelayChannelEntity;
import net.lab1024.sa.admin.module.business.ai.relay.domain.entity.RelayModelEntity;
import net.lab1024.sa.admin.module.business.ai.relay.dao.RelayModelDao;
import net.lab1024.sa.admin.module.business.ai.relay.provider.ProviderAdapter;
import net.lab1024.sa.admin.module.business.ai.relay.provider.ProviderAdapterFactory;
import net.lab1024.sa.admin.module.business.ai.relay.provider.UpstreamRequest;
import net.lab1024.sa.admin.module.business.ai.relay.provider.UpstreamResponse;
import net.lab1024.sa.admin.module.business.ai.relay.provider.UsageResult;
import net.lab1024.sa.admin.module.business.ai.relay.service.billing.BillingEngine;
import net.lab1024.sa.admin.module.business.ai.relay.service.crypto.ApiKeyCipher;
import net.lab1024.sa.admin.module.business.ai.relay.service.routing.ChannelRouter;
import net.lab1024.sa.admin.module.business.ai.relay.upstream.UpstreamCaller;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Set;

/**
 * Embedding 编排服务
 */
@Slf4j
@Service
public class RelayEmbeddingService {

    @Resource
    private RelayModelDao relayModelDao;

    @Resource
    private ChannelRouter channelRouter;

    @Resource
    private BillingEngine billingEngine;

    @Resource
    private ProviderAdapterFactory providerAdapterFactory;

    @Resource
    private ApiKeyCipher apiKeyCipher;

    @Resource
    private UpstreamCaller upstreamCaller;

    private static final int DEFAULT_MAX_RETRIES = 2;

    public String embeddings(String requestBody) {
        RelayRequestContext ctx = RelayRequestContext.get();
        long startTime = System.currentTimeMillis();

        // 1. 解析模型
        JSONObject reqJson = JSON.parseObject(requestBody);
        String modelName = reqJson.getString("model");
        RelayModelEntity model = relayModelDao.selectByModelName(modelName);
        if (model == null || model.getDisabledFlag() == 1) {
            return buildErrorJson("model_not_found", "模型不存在或已禁用: " + modelName);
        }

        // 2. 预扣
        long estimatedQuota = estimateQuota(requestBody, model);
        Long billingSessionId;
        try {
            billingSessionId = billingEngine.preconsume(
                    ctx.getEmployeeId(), ctx.getTokenId(), null,
                    model.getModelId(), EndpointTypeEnum.EMBEDDING,
                    ctx.getRequestId(), estimatedQuota);
        } catch (Exception e) {
            return buildErrorJson("insufficient_quota", "额度不足");
        }

        // 3. 重试循环
        Set<Long> excludeChannelIds = new HashSet<>();
        int maxRetries = DEFAULT_MAX_RETRIES;
        String lastError = null;

        for (int attempt = 0; attempt <= maxRetries; attempt++) {
            RelayChannelEntity channel = channelRouter.selectChannel(modelName, EndpointTypeEnum.EMBEDDING, excludeChannelIds);
            if (channel == null) {
                lastError = "无可用渠道";
                break;
            }
            if (attempt == 0 && channel.getMaxRetries() != null) {
                maxRetries = channel.getMaxRetries();
            }

            try {
                ProviderAdapter adapter = providerAdapterFactory.getAdapter(channel.getProviderType());
                String decryptedKey = apiKeyCipher.decrypt(channel.getApiKeyCipher(), channel.getApiKeyKid());
                UpstreamRequest upstreamReq = adapter.buildUpstreamRequest(channel, decryptedKey, requestBody, EndpointTypeEnum.EMBEDDING);
                UpstreamResponse upstreamResp = upstreamCaller.call(upstreamReq);

                long latencyMs = System.currentTimeMillis() - startTime;

                if (upstreamResp.getStatusCode() >= 200 && upstreamResp.getStatusCode() < 300) {
                    UsageResult usage = adapter.parseUsage(upstreamResp.getBody());
                    if (usage == null) {
                        usage = adapter.estimateUsage(requestBody, upstreamResp.getBody());
                    }
                    billingEngine.settle(billingSessionId, usage, model,
                            upstreamResp.getStatusCode(), 200, latencyMs, false, channel.getChannelId());
                    return upstreamResp.getBody();
                }

                lastError = adapter.parseErrorMessage(upstreamResp.getStatusCode(), upstreamResp.getBody());
                excludeChannelIds.add(channel.getChannelId());
            } catch (Exception e) {
                lastError = e.getMessage();
                excludeChannelIds.add(channel.getChannelId());
            }
        }

        long latencyMs = System.currentTimeMillis() - startTime;
        billingEngine.rollback(billingSessionId, 0, 502, latencyMs, lastError);
        return buildErrorJson("all_channels_failed", "所有渠道均不可用: " + lastError);
    }

    private long estimateQuota(String requestBody, RelayModelEntity model) {
        int estimatedTokens = Math.max(requestBody.length() / 4, 100);
        BigDecimal cost = model.getInputPricePerK()
                .multiply(BigDecimal.valueOf(estimatedTokens))
                .divide(BigDecimal.valueOf(1000), 6, RoundingMode.HALF_UP);
        return cost.setScale(0, RoundingMode.CEILING).longValue();
    }

    private String buildErrorJson(String code, String message) {
        return "{\"error\":{\"message\":\"" + message + "\",\"type\":\"invalid_request_error\",\"code\":\"" + code + "\"}}";
    }
}
