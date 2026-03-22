package net.lab1024.sa.admin.module.business.ai.merch.client;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.volcengine.ark.runtime.model.content.generation.CreateContentGenerationTaskRequest;
import com.volcengine.ark.runtime.model.content.generation.CreateContentGenerationTaskResult;
import com.volcengine.ark.runtime.model.content.generation.GetContentGenerationTaskRequest;
import com.volcengine.ark.runtime.model.content.generation.GetContentGenerationTaskResponse;
import com.volcengine.ark.runtime.model.content.generation.TaskStatus;
import com.volcengine.ark.runtime.model.images.generation.GenerateImagesRequest;
import com.volcengine.ark.runtime.model.images.generation.ImagesResponse;
import com.volcengine.ark.runtime.model.images.generation.ResponseFormat;
import com.volcengine.ark.runtime.service.ArkService;
import jakarta.annotation.PreDestroy;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.ai.model.domain.entity.AiModelConfigEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class VolcengineMediaClient {

    private static final Duration SDK_TIMEOUT = Duration.ofSeconds(60);

    private final Map<String, ArkService> arkServiceCache = new ConcurrentHashMap<>();

    public SubmitResult submitImage(AiModelConfigEntity config, String prompt) {
        try {
            JSONObject extraConfig = parseExtra(config.getExtraConfigJson());
            JSONObject requestConfig = mergeRequestConfig(extraConfig, "imageBody");

            GenerateImagesRequest request = new GenerateImagesRequest();
            request.setModel(config.getEndPointModeName());
            request.setPrompt(prompt);
            request.setResponseFormat(StringUtils.defaultIfBlank(getString(requestConfig, "responseFormat", "response_format"), ResponseFormat.Url));
            request.setSeed(getInteger(requestConfig, "seed"));
            request.setGuidanceScale(getDouble(requestConfig, "guidanceScale", "guidance_scale"));
            request.setSize(getString(requestConfig, "size"));
            request.setWatermark(getBoolean(requestConfig, "watermark"));
            request.setOptimizePrompt(getBoolean(requestConfig, "optimizePrompt", "optimize_prompt"));
            request.setSequentialImageGeneration(getString(requestConfig, "sequentialImageGeneration", "sequential_image_generation"));
            request.setOutputFormat(getString(requestConfig, "outputFormat", "output_format"));
            applyImageInput(request, requestConfig);
            applyImageOptions(request, requestConfig);

            ImagesResponse response = getArkService(config).generateImages(request, buildHeaders(extraConfig));
            if (response == null) {
                throw new RuntimeException("火山引擎图片生成未返回结果");
            }
            if (response.getError() != null && StringUtils.isNotBlank(response.getError().getMessage())) {
                throw new RuntimeException(response.getError().getMessage());
            }

            SubmitResult result = new SubmitResult();
            result.setCompleted(Boolean.TRUE);
            result.setMediaUrl(extractImageUrl(response));
            result.setRawResponse(JSON.toJSONString(response));
            return result;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            log.error("火山引擎图片生成失败", e);
            throw new RuntimeException("火山引擎图片生成失败：" + e.getMessage(), e);
        }
    }

    public SubmitResult submitVideo(AiModelConfigEntity config, String prompt, String imageUrl) {
        try {
            JSONObject extraConfig = parseExtra(config.getExtraConfigJson());
            JSONObject requestConfig = mergeRequestConfig(extraConfig, "videoBody");

            CreateContentGenerationTaskRequest request = new CreateContentGenerationTaskRequest();
            request.setModel(config.getEndPointModeName());
            request.setContent(buildVideoContent(prompt, imageUrl));
            request.setCallbackUrl(getString(requestConfig, "callbackUrl", "callback_url"));
            request.setReturnLastFrame(getBoolean(requestConfig, "returnLastFrame", "return_last_frame"));
            request.setServiceTier(getString(requestConfig, "serviceTier", "service_tier"));
            request.setExecutionExpiresAfter(getLong(requestConfig, "executionExpiresAfter", "execution_expires_after"));
            request.setGenerateAudio(getBoolean(requestConfig, "generateAudio", "generate_audio"));
            request.setCameraFixed(getBoolean(requestConfig, "cameraFixed", "camera_fixed"));
            request.setWatermark(getBoolean(requestConfig, "watermark"));
            request.setSeed(getLong(requestConfig, "seed"));
            request.setResolution(getString(requestConfig, "resolution"));
            request.setRatio(getString(requestConfig, "ratio"));
            request.setDuration(getLong(requestConfig, "duration"));
            request.setFrames(getLong(requestConfig, "frames"));
            request.setDraft(getBoolean(requestConfig, "draft"));

            CreateContentGenerationTaskResult response = getArkService(config).createContentGenerationTask(request, buildHeaders(extraConfig));
            if (response == null || StringUtils.isBlank(response.getId())) {
                throw new RuntimeException("火山引擎视频任务未返回任务号");
            }

            SubmitResult result = new SubmitResult();
            result.setCompleted(Boolean.FALSE);
            result.setTaskId(response.getId());
            result.setRawResponse(JSON.toJSONString(response));
            return result;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            log.error("火山引擎视频任务提交失败", e);
            throw new RuntimeException("火山引擎视频任务提交失败：" + e.getMessage(), e);
        }
    }

    public QueryResult queryTask(AiModelConfigEntity config, String taskId) {
        try {
            JSONObject extraConfig = parseExtra(config.getExtraConfigJson());
            GetContentGenerationTaskRequest request = new GetContentGenerationTaskRequest();
            request.setTaskId(taskId);

            GetContentGenerationTaskResponse response = getArkService(config).getContentGenerationTask(request, buildHeaders(extraConfig));
            QueryResult result = new QueryResult();
            result.setStatus(response.getStatus());

            String status = StringUtils.lowerCase(response.getStatus());
            String mediaUrl = null;
            if (response.getContent() != null) {
                mediaUrl = StringUtils.firstNonBlank(response.getContent().getVideoUrl(), response.getContent().getFileUrl());
            }
            String errorMessage = response.getError() == null ? null : StringUtils.firstNonBlank(response.getError().getMessage(), response.getError().getCode());

            boolean completed = StringUtils.equals(status, TaskStatus.SUCCEEDED) && StringUtils.isNotBlank(mediaUrl);
            boolean failed = StringUtils.equalsAny(status, TaskStatus.FAILED, TaskStatus.CANCELLED)
                    || (StringUtils.equals(status, TaskStatus.SUCCEEDED) && StringUtils.isBlank(mediaUrl));

            if (failed && StringUtils.isBlank(errorMessage) && StringUtils.equals(status, TaskStatus.SUCCEEDED)) {
                errorMessage = "火山引擎任务已完成但未返回视频地址";
            }

            result.setMediaUrl(mediaUrl);
            result.setCompleted(completed);
            result.setFailed(failed);
            result.setErrorMessage(errorMessage);
            return result;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            log.error("火山引擎任务查询失败", e);
            throw new RuntimeException("火山引擎任务查询失败：" + e.getMessage(), e);
        }
    }

    @PreDestroy
    public void destroy() {
        arkServiceCache.values().forEach(service -> {
            try {
                service.shutdownExecutor();
            } catch (Exception e) {
                log.debug("关闭火山引擎SDK客户端失败: {}", e.getMessage());
            }
        });
        arkServiceCache.clear();
    }

    private ArkService getArkService(AiModelConfigEntity config) {
        if (StringUtils.isBlank(config.getApiKey())) {
            throw new RuntimeException("火山引擎模型未配置API密钥");
        }
        String cacheKey = String.valueOf(Objects.hash(config.getId(), config.getUpdateTime(), config.getBaseUrl(), config.getApiKey()));
        return arkServiceCache.computeIfAbsent(cacheKey, key -> {
            ArkService.Builder builder = ArkService.builder()
                    .apiKey(config.getApiKey())
                    .timeout(SDK_TIMEOUT);
            if (StringUtils.isNotBlank(config.getBaseUrl())) {
                builder.baseUrl(config.getBaseUrl());
            }
            return builder.build();
        });
    }

    private List<CreateContentGenerationTaskRequest.Content> buildVideoContent(String prompt, String imageUrl) {
        List<CreateContentGenerationTaskRequest.Content> contentList = new ArrayList<>(2);

        CreateContentGenerationTaskRequest.Content textContent = new CreateContentGenerationTaskRequest.Content();
        textContent.setType("text");
        textContent.setRole("user");
        textContent.setText(prompt);
        contentList.add(textContent);

        CreateContentGenerationTaskRequest.Content imageContent = new CreateContentGenerationTaskRequest.Content();
        imageContent.setType("image_url");
        imageContent.setRole("user");
        CreateContentGenerationTaskRequest.ImageUrl sourceImage = new CreateContentGenerationTaskRequest.ImageUrl();
        sourceImage.setUrl(imageUrl);
        imageContent.setImageUrl(sourceImage);
        contentList.add(imageContent);
        return contentList;
    }

    private void applyImageInput(GenerateImagesRequest request, JSONObject requestConfig) {
        JSONArray imageArray = requestConfig.getJSONArray("image");
        if (imageArray != null && !imageArray.isEmpty()) {
            List<String> imageList = new ArrayList<>(imageArray.size());
            for (Object item : imageArray) {
                if (item != null && StringUtils.isNotBlank(String.valueOf(item))) {
                    imageList.add(String.valueOf(item));
                }
            }
            if (!imageList.isEmpty()) {
                request.setImage(imageList);
            }
            return;
        }
        String image = getString(requestConfig, "image");
        if (StringUtils.isNotBlank(image)) {
            request.setImage(image);
        }
    }

    private void applyImageOptions(GenerateImagesRequest request, JSONObject requestConfig) {
        JSONObject optimizePromptOptions = requestConfig.getJSONObject("optimizePromptOptions");
        if (optimizePromptOptions == null) {
            optimizePromptOptions = requestConfig.getJSONObject("optimize_prompt_options");
        }
        if (optimizePromptOptions != null) {
            GenerateImagesRequest.OptimizePromptOptions options = new GenerateImagesRequest.OptimizePromptOptions();
            options.setThinking(getString(optimizePromptOptions, "thinking"));
            options.setMode(getString(optimizePromptOptions, "mode"));
            request.setOptimizePromptOptions(options);
        }

        JSONObject sequentialOptions = requestConfig.getJSONObject("sequentialImageGenerationOptions");
        if (sequentialOptions == null) {
            sequentialOptions = requestConfig.getJSONObject("sequential_image_generation_options");
        }
        if (sequentialOptions != null) {
            GenerateImagesRequest.SequentialImageGenerationOptions options = new GenerateImagesRequest.SequentialImageGenerationOptions();
            options.setMaxImages(getInteger(sequentialOptions, "maxImages", "max_images"));
            request.setSequentialImageGenerationOptions(options);
        }

        JSONArray tools = requestConfig.getJSONArray("tools");
        if (tools == null || tools.isEmpty()) {
            return;
        }
        List<GenerateImagesRequest.ContentGenerationTool> toolList = new ArrayList<>(tools.size());
        for (Object item : tools) {
            String toolType = null;
            if (item instanceof JSONObject toolObject) {
                toolType = getString(toolObject, "type");
            } else if (item != null) {
                toolType = String.valueOf(item);
            }
            if (StringUtils.isBlank(toolType)) {
                continue;
            }
            GenerateImagesRequest.ContentGenerationTool tool = new GenerateImagesRequest.ContentGenerationTool();
            tool.setType(toolType);
            toolList.add(tool);
        }
        if (!toolList.isEmpty()) {
            request.setTools(toolList);
        }
    }

    private String extractImageUrl(ImagesResponse response) {
        if (response.getData() == null || response.getData().isEmpty() || response.getData().get(0) == null) {
            throw new RuntimeException("火山引擎图片生成未返回结果地址");
        }
        ImagesResponse.Image image = response.getData().get(0);
        if (StringUtils.isNotBlank(image.getUrl())) {
            return image.getUrl();
        }
        if (StringUtils.isNotBlank(image.getB64Json())) {
            throw new RuntimeException("火山引擎图片生成返回了Base64结果，当前流程仅支持URL结果，请将responseFormat配置为url");
        }
        throw new RuntimeException("火山引擎图片生成未返回结果地址");
    }

    private Map<String, String> buildHeaders(JSONObject extraConfig) {
        Map<String, String> headers = new HashMap<>(4);
        JSONObject headerJson = extraConfig.getJSONObject("headers");
        if (headerJson == null) {
            return headers;
        }
        for (Map.Entry<String, Object> entry : headerJson.entrySet()) {
            headers.put(entry.getKey(), String.valueOf(entry.getValue()));
        }
        return headers;
    }

    private JSONObject mergeRequestConfig(JSONObject extraConfig, String sceneKey) {
        JSONObject merged = new JSONObject();
        merged.putAll(extraConfig);
        JSONObject submitBody = extraConfig.getJSONObject("submitBody");
        if (submitBody != null) {
            merged.putAll(submitBody);
        }
        JSONObject sceneBody = extraConfig.getJSONObject(sceneKey);
        if (sceneBody != null) {
            merged.putAll(sceneBody);
        }
        return merged;
    }

    private JSONObject parseExtra(String extraConfigJson) {
        if (StringUtils.isBlank(extraConfigJson)) {
            return new JSONObject();
        }
        try {
            return JSON.parseObject(extraConfigJson);
        } catch (Exception e) {
            log.warn("extraConfigJson解析失败: {}", e.getMessage());
            return new JSONObject();
        }
    }

    private Object getValue(JSONObject jsonObject, String... keys) {
        if (jsonObject == null || keys == null) {
            return null;
        }
        for (String key : keys) {
            Object value = jsonObject.get(key);
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    private String getString(JSONObject jsonObject, String... keys) {
        Object value = getValue(jsonObject, keys);
        return value == null ? null : String.valueOf(value);
    }

    private Boolean getBoolean(JSONObject jsonObject, String... keys) {
        Object value = getValue(jsonObject, keys);
        if (value == null) {
            return null;
        }
        if (value instanceof Boolean booleanValue) {
            return booleanValue;
        }
        return Boolean.valueOf(String.valueOf(value));
    }

    private Integer getInteger(JSONObject jsonObject, String... keys) {
        Object value = getValue(jsonObject, keys);
        if (value == null || StringUtils.isBlank(String.valueOf(value))) {
            return null;
        }
        if (value instanceof Number number) {
            return number.intValue();
        }
        return Integer.valueOf(String.valueOf(value));
    }

    private Long getLong(JSONObject jsonObject, String... keys) {
        Object value = getValue(jsonObject, keys);
        if (value == null || StringUtils.isBlank(String.valueOf(value))) {
            return null;
        }
        if (value instanceof Number number) {
            return number.longValue();
        }
        return Long.valueOf(String.valueOf(value));
    }

    private Double getDouble(JSONObject jsonObject, String... keys) {
        Object value = getValue(jsonObject, keys);
        if (value == null || StringUtils.isBlank(String.valueOf(value))) {
            return null;
        }
        if (value instanceof Number number) {
            return number.doubleValue();
        }
        return Double.valueOf(String.valueOf(value));
    }

    @Data
    public static class SubmitResult {

        private String taskId;

        private String mediaUrl;

        private Boolean completed;

        private String rawResponse;
    }

    @Data
    public static class QueryResult {

        private String status;

        private String mediaUrl;

        private Boolean completed;

        private Boolean failed;

        private String errorMessage;
    }
}
