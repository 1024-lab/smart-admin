package net.lab1024.sa.admin.module.business.ai.merch.client;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.responses.EasyInputMessage;
import com.openai.models.responses.Response;
import com.openai.models.responses.ResponseCreateParams;
import com.openai.models.responses.ResponseInputContent;
import com.openai.models.responses.ResponseInputImage;
import com.openai.models.responses.ResponseInputItem;
import com.openai.models.responses.ResponseInputText;
import com.openai.models.responses.ResponseOutputItem;
import com.openai.models.responses.ResponseStatus;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.ai.model.domain.entity.AiModelConfigEntity;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.net.URLConnection;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Component
public class OpenAiPromptClient {

    private static final Duration IMAGE_DOWNLOAD_TIMEOUT = Duration.ofSeconds(60);

    private static final Duration SDK_TIMEOUT = Duration.ofSeconds(60);

    private static final String DEFAULT_IMAGE_MIME_TYPE = "image/jpeg";

    private static final int MAX_HTTP_LOG_LENGTH = 20000;

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(IMAGE_DOWNLOAD_TIMEOUT)
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build();

    private final Map<String, OpenAIClient> clientCache = new ConcurrentHashMap<>();

    public String generatePrompt(AiModelConfigEntity config, String promptTemplate, String imageUrl) {
        if (config == null) {
            throw new RuntimeException("OpenAI 模型配置不存在");
        }
        if (StringUtils.isBlank(config.getApiKey())) {
            throw new RuntimeException("OpenAI 模型未配置 API 密钥");
        }
        if (StringUtils.isBlank(imageUrl)) {
            throw new RuntimeException("图片地址不能为空");
        }

        String model = null;
        String endpoint = null;
        String requestBodyLog = null;

        try {
            JSONObject extraConfig = parseExtra(config.getExtraConfigJson());
            model = resolveModel(config);
            endpoint = resolveResponsesEndpoint(config);
            String imageDataUrl = toImageDataUrl(imageUrl);

//            ResponseCreateParams.Builder requestBuilder = ResponseCreateParams.builder()
//                    .model(model)
//                    .inputOfResponse(List.of(ResponseInputItem.ofEasyInputMessage(
//                            buildUserMessage(promptTemplate, imageDataUrl, extraConfig)
//                    )));

            if (config.getMaxTokens() != null && config.getMaxTokens() > 0) {
//                requestBuilder.maxOutputTokens(config.getMaxTokens().longValue());
            }

            ResponseCreateParams params = ResponseCreateParams.builder()
                    .model(model)
                    .input(Arrays.asList(
                            Map.of(
                                    "role", "user",
                                    "content", List.of(
                                            Map.of(
                                                    "type", "input_image",
                                                    "image_url", imageDataUrl
                                            ),
                                            Map.of(
                                                    "type", "input_text",
                                                    "text", promptTemplate
                                            )
                                    )
                            )
                    ).toString())
                    .build();
            requestBodyLog = sanitizeHttpLog(toJsonForLog(params));

            log.info("OpenAI Responses request, modelId={}, model={}, method=POST, url={}, headers={}, body={}",
                    config.getId(),
                    model,
                    endpoint,
                    buildRequestHeadersLog(config, extraConfig),
                    requestBodyLog);

            long startAt = System.currentTimeMillis();
            var rawResponse = getClient(config, extraConfig)
                    .withRawResponse()
                    .responses()
                    .create(params);
            Response response = rawResponse.parse();

            log.info("OpenAI Responses response, modelId={}, model={}, method=POST, url={}, durationMs={}, statusCode={}, requestId={}, headers={}, body={}",
                    config.getId(),
                    model,
                    endpoint,
                    System.currentTimeMillis() - startAt,
                    rawResponse.statusCode(),
                    rawResponse.requestId().orElse(null),
                    rawResponse.headers(),
                    sanitizeHttpLog(toJsonForLog(response)));

            return extractOutputText(response);
        } catch (Exception e) {
            log.error("OpenAI 图生文生成提示词失败, modelId={}, model={}, method=POST, url={}, imageUrl={}, requestBody={}",
                    config.getId(),
                    model,
                    endpoint,
                    imageUrl,
                    requestBodyLog,
                    e);
            if (e instanceof RuntimeException runtimeException) {
                throw runtimeException;
            }
            throw new RuntimeException("OpenAI 图生文生成提示词失败：" + e.getMessage(), e);
        }
    }

    @PreDestroy
    public void destroy() {
        clientCache.values().forEach(client -> {
            try {
                client.close();
            } catch (Exception e) {
                log.debug("关闭 OpenAI 客户端失败: {}", e.getMessage());
            }
        });
        clientCache.clear();
    }

    private OpenAIClient getClient(AiModelConfigEntity config, JSONObject extraConfig) {
        String cacheKey = String.valueOf(Objects.hash(
                config.getId(),
                config.getUpdateTime(),
                config.getBaseUrl(),
                config.getApiKey(),
                config.getExtraConfigJson()
        ));
        return clientCache.computeIfAbsent(cacheKey, key -> {
            OpenAIOkHttpClient.Builder builder = OpenAIOkHttpClient.builder()
                    .apiKey(config.getApiKey())
                    .timeout(Duration.ofSeconds(getLong(extraConfig, "timeoutSeconds", "timeout_seconds", SDK_TIMEOUT.getSeconds())));

            Integer maxRetries = getInteger(extraConfig, "maxRetries", "max_retries");
            if (maxRetries != null && maxRetries >= 0) {
                builder.maxRetries(maxRetries);
            }

            String baseUrl = StringUtils.trimToNull(config.getBaseUrl());
            if (baseUrl != null) {
                builder.baseUrl(baseUrl);
            }

            String organization = getString(extraConfig, "organization");
            if (StringUtils.isNotBlank(organization)) {
                builder.organization(organization);
            }

            String project = getString(extraConfig, "project");
            if (StringUtils.isNotBlank(project)) {
                builder.project(project);
            }

            return builder.build();
        });
    }

    private EasyInputMessage buildUserMessage(String promptTemplate, String imageDataUrl, JSONObject extraConfig) {
        List<ResponseInputContent> contentList = new ArrayList<>(2);
        if (StringUtils.isNotBlank(promptTemplate)) {
            contentList.add(ResponseInputContent.ofInputText(
                    ResponseInputText.builder()
                            .text(promptTemplate)
                            .build()
            ));
        }

        contentList.add(ResponseInputContent.ofInputImage(
                ResponseInputImage.builder()
                        .detail(resolveImageDetail(extraConfig))
                        .imageUrl(imageDataUrl)
                        .build()
        ));

        return EasyInputMessage.builder()
                .role(EasyInputMessage.Role.USER)
                .contentOfResponseInputMessageContentList(contentList)
                .build();
    }

    private String extractOutputText(Response response) {
        if (response == null) {
            throw new RuntimeException("OpenAI 未返回响应结果");
        }

        if (response.error().isPresent() && StringUtils.isNotBlank(response.error().get().message())) {
            throw new RuntimeException(response.error().get().message());
        }

        if (response.status().isPresent()) {
            ResponseStatus status = response.status().get();
            if (Objects.equals(status, ResponseStatus.FAILED)) {
                throw new RuntimeException(resolveResponseFailureMessage(response));
            }
            if (Objects.equals(status, ResponseStatus.INCOMPLETE)) {
                throw new RuntimeException(resolveResponseIncompleteMessage(response));
            }
            if (Objects.equals(status, ResponseStatus.CANCELLED)) {
                throw new RuntimeException("OpenAI 响应已取消");
            }
            if (Objects.equals(status, ResponseStatus.QUEUED) || Objects.equals(status, ResponseStatus.IN_PROGRESS)) {
                throw new RuntimeException("OpenAI 响应尚未完成，当前状态：" + status);
            }
        }

        String outputText = response.output().stream()
                .filter(ResponseOutputItem::isMessage)
                .map(ResponseOutputItem::asMessage)
                .flatMap(item -> item.content().stream())
                .filter(item -> item.outputText().isPresent())
                .map(item -> item.asOutputText().text())
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.joining("\n"));

        if (StringUtils.isBlank(outputText)) {
            throw new RuntimeException(resolveResponseFailureMessage(response));
        }
        return outputText.trim();
    }

    private String resolveResponseFailureMessage(Response response) {
        if (response != null && response.error().isPresent() && StringUtils.isNotBlank(response.error().get().message())) {
            return response.error().get().message();
        }

        if (response != null) {
            String refusal = response.output().stream()
                    .filter(ResponseOutputItem::isMessage)
                    .map(ResponseOutputItem::asMessage)
                    .flatMap(item -> item.content().stream())
                    .filter(item -> item.refusal().isPresent())
                    .map(item -> item.asRefusal().refusal())
                    .filter(StringUtils::isNotBlank)
                    .collect(Collectors.joining("\n"));
            if (StringUtils.isNotBlank(refusal)) {
                return refusal;
            }
        }

        if (response != null && response.status().isPresent()) {
            return "OpenAI 响应失败，状态：" + response.status().get();
        }
        return "OpenAI 未返回可用文本结果";
    }

    private String resolveResponseIncompleteMessage(Response response) {
        if (response != null && response.incompleteDetails().isPresent()) {
            return "OpenAI 响应不完整：" + response.incompleteDetails().get();
        }
        return "OpenAI 响应不完整";
    }

    private ResponseInputImage.Detail resolveImageDetail(JSONObject extraConfig) {
        String detail = StringUtils.defaultIfBlank(
                getString(extraConfig, "detail", "imageDetail", "image_detail"),
                "auto"
        );
        return ResponseInputImage.Detail.of(StringUtils.lowerCase(detail));
    }

    private String toImageDataUrl(String imageUrl) {
        if (StringUtils.startsWithIgnoreCase(imageUrl, "data:")) {
            return imageUrl;
        }

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(imageUrl))
                    .timeout(IMAGE_DOWNLOAD_TIMEOUT)
                    .GET()
                    .build();
            HttpResponse<byte[]> response = httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray());
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new RuntimeException("下载图片失败，HTTP 状态码：" + response.statusCode());
            }
            byte[] bytes = response.body();
            if (bytes == null || bytes.length == 0) {
                throw new RuntimeException("下载图片失败，图片内容为空");
            }

            String mimeType = resolveImageMimeType(
                    response.headers().firstValue("content-type").orElse(null),
                    imageUrl,
                    bytes
            );
            return "data:" + mimeType + ";base64," + Base64.getEncoder().encodeToString(bytes);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("下载图片并转换 Base64 失败：" + e.getMessage(), e);
        }
    }

    private String resolveImageMimeType(String contentType, String imageUrl, byte[] bytes) {
        String normalizedContentType = normalizeContentType(contentType);
        if (StringUtils.isNotBlank(normalizedContentType)) {
            return normalizedContentType;
        }

        try {
            String guessedFromStream = URLConnection.guessContentTypeFromStream(new ByteArrayInputStream(bytes));
            String normalizedFromStream = normalizeContentType(guessedFromStream);
            if (StringUtils.isNotBlank(normalizedFromStream)) {
                return normalizedFromStream;
            }
        } catch (Exception e) {
            log.debug("根据图片流推断 MIME 失败: {}", e.getMessage());
        }

        String extension = StringUtils.lowerCase(FilenameUtils.getExtension(URI.create(imageUrl).getPath()));
        return switch (extension) {
            case "png" -> "image/png";
            case "gif" -> "image/gif";
            case "webp" -> "image/webp";
            case "bmp" -> "image/bmp";
            case "svg" -> "image/svg+xml";
            case "jpg", "jpeg" -> "image/jpeg";
            default -> DEFAULT_IMAGE_MIME_TYPE;
        };
    }

    private String normalizeContentType(String contentType) {
        String normalized = StringUtils.trimToNull(contentType);
        if (normalized == null) {
            return null;
        }
        int separatorIndex = normalized.indexOf(';');
        if (separatorIndex >= 0) {
            normalized = normalized.substring(0, separatorIndex);
        }
        normalized = StringUtils.lowerCase(StringUtils.trim(normalized));
        return StringUtils.startsWith(normalized, "image/") ? normalized : null;
    }

    private String resolveModel(AiModelConfigEntity config) {
        String model = StringUtils.firstNonBlank(config.getEndPointModeName(), config.getModelName());
        if (StringUtils.isBlank(model)) {
            throw new RuntimeException("OpenAI 模型名称不能为空");
        }
        return model;
    }

    private String resolveResponsesEndpoint(AiModelConfigEntity config) {
        String baseUrl = StringUtils.removeEnd(
                StringUtils.defaultIfBlank(StringUtils.trimToNull(config.getBaseUrl()), "https://api.openai.com/v1"),
                "/"
        );
        return baseUrl + "/responses";
    }

    private String buildRequestHeadersLog(AiModelConfigEntity config, JSONObject extraConfig) {
        JSONObject headers = new JSONObject();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Bearer " + maskApiKey(config.getApiKey()));

        String organization = getString(extraConfig, "organization");
        if (StringUtils.isNotBlank(organization)) {
            headers.put("OpenAI-Organization", organization);
        }

        String project = getString(extraConfig, "project");
        if (StringUtils.isNotBlank(project)) {
            headers.put("OpenAI-Project", project);
        }
        return headers.toJSONString();
    }

    private String maskApiKey(String apiKey) {
        String normalizedApiKey = StringUtils.trimToEmpty(apiKey);
        if (normalizedApiKey.length() <= 8) {
            return "****";
        }
        return normalizedApiKey.substring(0, 4) + "****" + normalizedApiKey.substring(normalizedApiKey.length() - 4);
    }

    private String toJsonForLog(Object object) {
        if (object == null) {
            return null;
        }
        try {
            return JSON.toJSONString(object);
        } catch (Exception e) {
            log.debug("OpenAI 日志对象转 JSON 失败: {}", e.getMessage());
            return String.valueOf(object);
        }
    }

    private String sanitizeHttpLog(String logText) {
        if (StringUtils.isBlank(logText)) {
            return logText;
        }
        String sanitized = logText.replaceAll("(data:image/[^;]+;base64,)[A-Za-z0-9+/=]+", "$1<base64 omitted>");
        return StringUtils.abbreviate(sanitized, MAX_HTTP_LOG_LENGTH);
    }

    private JSONObject parseExtra(String extraConfigJson) {
        if (StringUtils.isBlank(extraConfigJson)) {
            return new JSONObject();
        }
        try {
            JSONObject jsonObject = JSON.parseObject(extraConfigJson);
            return jsonObject == null ? new JSONObject() : jsonObject;
        } catch (Exception e) {
            log.warn("OpenAI extraConfigJson 解析失败: {}", e.getMessage());
            return new JSONObject();
        }
    }

    private String getString(JSONObject jsonObject, String... keys) {
        if (jsonObject == null || keys == null) {
            return null;
        }
        for (String key : keys) {
            String value = jsonObject.getString(key);
            if (StringUtils.isNotBlank(value)) {
                return value;
            }
        }
        return null;
    }

    private Integer getInteger(JSONObject jsonObject, String... keys) {
        if (jsonObject == null || keys == null) {
            return null;
        }
        for (String key : keys) {
            Integer value = jsonObject.getInteger(key);
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    private Long getLong(JSONObject jsonObject, String firstKey, String secondKey, Long defaultValue) {
        if (jsonObject == null) {
            return defaultValue;
        }
        Long value = jsonObject.getLong(firstKey);
        if (value == null && StringUtils.isNotBlank(secondKey)) {
            value = jsonObject.getLong(secondKey);
        }
        return value == null ? defaultValue : value;
    }
}
