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
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import jakarta.annotation.Resource;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;

/**
 * Chat Completions 核心编排服务
 * <p>
 * 链路: 解析模型 → 选渠道 → 预扣额度 → 转发上游 → 解析usage → 结算/退款 → 记录日志
 */
@Slf4j
@Service
public class RelayChatService {

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

    @Resource(name = "relayStreamExecutor")
    private ExecutorService relayStreamExecutor;

    private static final int DEFAULT_MAX_RETRIES = 2;

    /**
     * 非流式 chat completions
     */
    public String chatCompletions(String requestBody) {
        RelayRequestContext ctx = RelayRequestContext.get();
        long startTime = System.currentTimeMillis();

        // 1. 解析模型
        JSONObject reqJson = JSON.parseObject(requestBody);
        String modelName = reqJson.getString("model");
        RelayModelEntity model = relayModelDao.selectByModelName(modelName);
        if (model == null || model.getDisabledFlag() == 1) {
            return buildErrorJson("model_not_found", "模型不存在或已禁用: " + modelName);
        }

        // 2. 估算预扣额度
        long estimatedQuota = estimateQuota(requestBody, model);

        // 3. 预扣
        Long billingSessionId;
        try {
            billingSessionId = billingEngine.preconsume(
                    ctx.getEmployeeId(), ctx.getTokenId(), null,
                    model.getModelId(), EndpointTypeEnum.CHAT,
                    ctx.getRequestId(), estimatedQuota);
        } catch (Exception e) {
            return buildErrorJson("insufficient_quota", "额度不足");
        }

        // 4. 重试循环选渠道 + 转发
        Set<Long> excludeChannelIds = new HashSet<>();
        int maxRetries = DEFAULT_MAX_RETRIES;
        String lastError = null;

        for (int attempt = 0; attempt <= maxRetries; attempt++) {
            RelayChannelEntity channel = channelRouter.selectChannel(modelName, EndpointTypeEnum.CHAT, excludeChannelIds);
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
                UpstreamRequest upstreamReq = adapter.buildUpstreamRequest(channel, decryptedKey, requestBody, EndpointTypeEnum.CHAT);
                UpstreamResponse upstreamResp = upstreamCaller.call(upstreamReq);

                long latencyMs = System.currentTimeMillis() - startTime;

                if (upstreamResp.getStatusCode() >= 200 && upstreamResp.getStatusCode() < 300) {
                    // 成功 — 解析 usage 并结算
                    UsageResult usage = adapter.parseUsage(upstreamResp.getBody());
                    if (usage == null) {
                        usage = adapter.estimateUsage(requestBody, upstreamResp.getBody());
                    }
                    billingEngine.settle(billingSessionId, usage, model,
                            upstreamResp.getStatusCode(), 200, latencyMs, false, channel.getChannelId());
                    return upstreamResp.getBody();
                }

                // 上游返回错误
                lastError = adapter.parseErrorMessage(upstreamResp.getStatusCode(), upstreamResp.getBody());
                excludeChannelIds.add(channel.getChannelId());
                log.warn("渠道[{}]调用失败, status={}, attempt={}, error={}",
                        channel.getChannelName(), upstreamResp.getStatusCode(), attempt, lastError);

            } catch (Exception e) {
                lastError = e.getMessage();
                excludeChannelIds.add(channel.getChannelId());
                log.warn("渠道[{}]调用异常, attempt={}, error={}",
                        channel.getChannelName(), attempt, e.getMessage());
            }
        }

        // 全部重试耗尽 — 回滚
        long latencyMs = System.currentTimeMillis() - startTime;
        billingEngine.rollback(billingSessionId, 0, 502, latencyMs, lastError);
        return buildErrorJson("all_channels_failed", "所有渠道均不可用: " + lastError);
    }

    /**
     * 流式 chat completions，返回 SseEmitter
     */
    public SseEmitter chatCompletionsStream(String requestBody) {
        RelayRequestContext ctx = RelayRequestContext.get();
        // 复制上下文到异步线程
        Long employeeId = ctx.getEmployeeId();
        Long tokenId = ctx.getTokenId();
        String requestId = ctx.getRequestId();

        SseEmitter emitter = new SseEmitter(120_000L);

        // 1. 解析模型
        JSONObject reqJson = JSON.parseObject(requestBody);
        String modelName = reqJson.getString("model");
        RelayModelEntity model = relayModelDao.selectByModelName(modelName);
        if (model == null || model.getDisabledFlag() == 1) {
            sendErrorAndComplete(emitter, "model_not_found", "模型不存在或已禁用: " + modelName);
            return emitter;
        }

        // 2. 预扣
        long estimatedQuota = estimateQuota(requestBody, model);
        Long billingSessionId;
        try {
            billingSessionId = billingEngine.preconsume(
                    employeeId, tokenId, null,
                    model.getModelId(), EndpointTypeEnum.CHAT,
                    requestId, estimatedQuota);
        } catch (Exception e) {
            sendErrorAndComplete(emitter, "insufficient_quota", "额度不足");
            return emitter;
        }

        // 3. 异步执行流式转发
        final RelayModelEntity finalModel = model;
        final Long finalBillingSessionId = billingSessionId;

        relayStreamExecutor.execute(() -> {
            long startTime = System.currentTimeMillis();
            Set<Long> excludeChannelIds = new HashSet<>();
            int maxRetries = DEFAULT_MAX_RETRIES;
            String lastError = null;
            boolean settled = false;

            for (int attempt = 0; attempt <= maxRetries && !settled; attempt++) {
                RelayChannelEntity channel = channelRouter.selectChannel(modelName, EndpointTypeEnum.CHAT, excludeChannelIds);
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
                    UpstreamRequest upstreamReq = adapter.buildUpstreamRequest(channel, decryptedKey, requestBody, EndpointTypeEnum.CHAT);
                    UpstreamResponse upstreamResp = upstreamCaller.callStream(upstreamReq);

                    if (upstreamResp.getStatusCode() >= 200 && upstreamResp.getStatusCode() < 300) {
                        // 流式读取并透传
                        settled = streamRelay(emitter, upstreamResp.getInputStream(), adapter,
                                finalBillingSessionId, finalModel, requestBody, channel,
                                startTime, upstreamResp.getStatusCode());
                        if (settled) {
                            return;
                        }
                    }

                    lastError = "上游返回HTTP " + upstreamResp.getStatusCode();
                    excludeChannelIds.add(channel.getChannelId());
                } catch (Exception e) {
                    lastError = e.getMessage();
                    excludeChannelIds.add(channel.getChannelId());
                    log.warn("流式渠道[{}]调用异常, attempt={}, error={}",
                            channel.getChannelName(), attempt, e.getMessage());
                }
            }

            // 全部失败
            long latencyMs = System.currentTimeMillis() - startTime;
            billingEngine.rollback(finalBillingSessionId, 0, 502, latencyMs, lastError);
            sendErrorAndComplete(emitter, "all_channels_failed", "所有渠道均不可用: " + lastError);
        });

        return emitter;
    }

    private boolean streamRelay(SseEmitter emitter, InputStream inputStream,
                                ProviderAdapter adapter, Long billingSessionId,
                                RelayModelEntity model, String requestBody,
                                RelayChannelEntity channel, long startTime, int upstreamStatus) {
        StringBuilder aggregatedText = new StringBuilder();
        String lastDataLine = null;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("data: ")) {
                    lastDataLine = line;
                    String data = line.substring(6).trim();
                    if ("[DONE]".equals(data)) {
                        emitter.send(SseEmitter.event().data("[DONE]"));
                        break;
                    }
                    // 聚合 content 文本
                    try {
                        JSONObject chunk = JSON.parseObject(data);
                        if (chunk.containsKey("choices")) {
                            JSONObject delta = chunk.getJSONArray("choices")
                                    .getJSONObject(0).getJSONObject("delta");
                            if (delta != null && delta.containsKey("content")) {
                                aggregatedText.append(delta.getString("content"));
                            }
                        }
                    } catch (Exception ignored) {
                    }
                    emitter.send(SseEmitter.event().data(data));
                } else if (!line.isEmpty()) {
                    // 透传非 data 行（如 event: 等）
                    emitter.send(SseEmitter.event().data(line));
                }
            }

            // 结算
            long latencyMs = System.currentTimeMillis() - startTime;
            UsageResult usage = adapter.parseStreamUsage(lastDataLine);
            if (usage == null) {
                usage = adapter.estimateUsage(requestBody, aggregatedText.toString());
            }
            billingEngine.settle(billingSessionId, usage, model,
                    upstreamStatus, 200, latencyMs, true, channel.getChannelId());
            emitter.complete();
            return true;

        } catch (Exception e) {
            log.error("流式转发异常: {}", e.getMessage());
            try {
                emitter.completeWithError(e);
            } catch (Exception ignored) {
            }
            return false;
        }
    }

    private long estimateQuota(String requestBody, RelayModelEntity model) {
        int estimatedInputTokens = Math.max(requestBody.length() / 4, 100);
        int estimatedOutputTokens = 2000;
        BigDecimal inputCost = model.getInputPricePerK()
                .multiply(BigDecimal.valueOf(estimatedInputTokens))
                .divide(BigDecimal.valueOf(1000), 6, RoundingMode.HALF_UP);
        BigDecimal outputCost = model.getOutputPricePerK()
                .multiply(BigDecimal.valueOf(estimatedOutputTokens))
                .divide(BigDecimal.valueOf(1000), 6, RoundingMode.HALF_UP);
        return inputCost.add(outputCost).setScale(0, RoundingMode.CEILING).longValue();
    }

    private String buildErrorJson(String code, String message) {
        return "{\"error\":{\"message\":\"" + message + "\",\"type\":\"invalid_request_error\",\"code\":\"" + code + "\"}}";
    }

    private void sendErrorAndComplete(SseEmitter emitter, String code, String message) {
        try {
            emitter.send(SseEmitter.event().data(buildErrorJson(code, message)));
            emitter.complete();
        } catch (Exception ignored) {
        }
    }
}
