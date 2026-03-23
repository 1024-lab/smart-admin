package net.lab1024.sa.admin.module.business.ai.relay.provider.openai;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.ai.relay.constant.EndpointTypeEnum;
import net.lab1024.sa.admin.module.business.ai.relay.constant.ProviderTypeEnum;
import net.lab1024.sa.admin.module.business.ai.relay.domain.entity.RelayChannelEntity;
import net.lab1024.sa.admin.module.business.ai.relay.provider.ProviderAdapter;
import net.lab1024.sa.admin.module.business.ai.relay.provider.UpstreamRequest;
import net.lab1024.sa.admin.module.business.ai.relay.provider.UsageResult;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Component
public class OpenAiProviderAdapter implements ProviderAdapter {

    @Override
    public ProviderTypeEnum getProviderType() {
        return ProviderTypeEnum.OPENAI;
    }

    @Override
    public boolean supportsEndpoint(EndpointTypeEnum endpointType) {
        return endpointType == EndpointTypeEnum.CHAT
                || endpointType == EndpointTypeEnum.EMBEDDING
                || endpointType == EndpointTypeEnum.BOTH;
    }

    @Override
    public UpstreamRequest buildUpstreamRequest(RelayChannelEntity channel, String decryptedApiKey,
                                                 String requestBody, EndpointTypeEnum endpointType) {
        UpstreamRequest req = new UpstreamRequest();

        String baseUrl = channel.getBaseUrl();
        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }

        String path;
        if (endpointType == EndpointTypeEnum.CHAT) {
            path = "/v1/chat/completions";
        } else if (endpointType == EndpointTypeEnum.EMBEDDING) {
            path = "/v1/embeddings";
        } else {
            path = "/v1/chat/completions";
        }

        // 如果 baseUrl 已经包含 /v1，则去掉 path 中的 /v1
        if (baseUrl.endsWith("/v1")) {
            path = path.substring(3);
        }

        req.setUrl(baseUrl + path);
        req.setMethod("POST");
        req.setBody(requestBody);
        req.setTimeoutMs(channel.getTimeoutMs() != null ? channel.getTimeoutMs() : 60000);

        Map<String, String> headers = new LinkedHashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Bearer " + decryptedApiKey);
        if (channel.getOrganization() != null && !channel.getOrganization().isBlank()) {
            headers.put("OpenAI-Organization", channel.getOrganization());
        }
        req.setHeaders(headers);

        return req;
    }

    @Override
    public UsageResult parseUsage(String responseBody) {
        try {
            JSONObject json = JSON.parseObject(responseBody);
            JSONObject usage = json.getJSONObject("usage");
            if (usage == null) {
                return null;
            }
            int prompt = usage.getIntValue("prompt_tokens", 0);
            int completion = usage.getIntValue("completion_tokens", 0);
            return UsageResult.of(prompt, completion);
        } catch (Exception e) {
            log.warn("解析OpenAI usage失败: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public UsageResult parseStreamUsage(String lastDataChunk) {
        try {
            if (lastDataChunk == null || lastDataChunk.isBlank()) {
                return null;
            }
            String data = lastDataChunk.trim();
            if (data.startsWith("data: ")) {
                data = data.substring(6);
            }
            if ("[DONE]".equals(data)) {
                return null;
            }
            JSONObject json = JSON.parseObject(data);
            JSONObject usage = json.getJSONObject("usage");
            if (usage == null) {
                return null;
            }
            int prompt = usage.getIntValue("prompt_tokens", 0);
            int completion = usage.getIntValue("completion_tokens", 0);
            return UsageResult.of(prompt, completion);
        } catch (Exception e) {
            log.warn("解析OpenAI stream usage失败: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public UsageResult estimateUsage(String requestBody, String aggregatedResponseText) {
        // 粗略估算: 1 token ≈ 4 字符(英文) 或 1.5 字符(中文混合)
        int promptTokens = estimateTokenCount(requestBody);
        int completionTokens = estimateTokenCount(aggregatedResponseText);
        return UsageResult.of(promptTokens, completionTokens);
    }

    @Override
    public String parseErrorMessage(int statusCode, String responseBody) {
        try {
            JSONObject json = JSON.parseObject(responseBody);
            JSONObject error = json.getJSONObject("error");
            if (error != null) {
                return error.getString("message");
            }
        } catch (Exception ignored) {
        }
        return "上游返回HTTP " + statusCode;
    }

    private int estimateTokenCount(String text) {
        if (text == null || text.isEmpty()) {
            return 0;
        }
        // 简单估算：中文字符按1.5个token/字，英文按4字符/token
        int chineseCount = 0;
        int otherCount = 0;
        for (char c : text.toCharArray()) {
            if (Character.UnicodeScript.of(c) == Character.UnicodeScript.HAN) {
                chineseCount++;
            } else {
                otherCount++;
            }
        }
        return (int) (chineseCount * 1.5 + otherCount / 4.0);
    }
}
