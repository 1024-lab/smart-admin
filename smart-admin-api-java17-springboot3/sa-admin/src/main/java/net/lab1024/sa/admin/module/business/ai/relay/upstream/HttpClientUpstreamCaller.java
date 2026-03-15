package net.lab1024.sa.admin.module.business.ai.relay.upstream;

import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.ai.relay.provider.UpstreamRequest;
import net.lab1024.sa.admin.module.business.ai.relay.provider.UpstreamResponse;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;

/**
 * 基于 JDK HttpClient 的上游调用实现，同时支持非流式和流式
 */
@Slf4j
@Component
public class HttpClientUpstreamCaller implements UpstreamCaller {

    private final HttpClient httpClient;

    public HttpClientUpstreamCaller() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    @Override
    public UpstreamResponse call(UpstreamRequest request) {
        try {
            HttpRequest.Builder builder = HttpRequest.newBuilder()
                    .uri(URI.create(request.getUrl()))
                    .timeout(Duration.ofMillis(request.getTimeoutMs()))
                    .POST(HttpRequest.BodyPublishers.ofString(request.getBody(), StandardCharsets.UTF_8));

            applyHeaders(builder, request.getHeaders());

            HttpResponse<String> httpResponse = httpClient.send(builder.build(),
                    HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

            UpstreamResponse resp = new UpstreamResponse();
            resp.setStatusCode(httpResponse.statusCode());
            resp.setBody(httpResponse.body());
            resp.setStream(false);
            return resp;
        } catch (Exception e) {
            log.error("非流式调用上游失败: url={}, error={}", request.getUrl(), e.getMessage());
            UpstreamResponse resp = new UpstreamResponse();
            resp.setStatusCode(0);
            resp.setBody("{\"error\":{\"message\":\"上游连接失败: " + e.getMessage() + "\",\"type\":\"upstream_error\",\"code\":\"upstream_connection_error\"}}");
            resp.setStream(false);
            return resp;
        }
    }

    @Override
    public UpstreamResponse callStream(UpstreamRequest request) {
        try {
            HttpRequest.Builder builder = HttpRequest.newBuilder()
                    .uri(URI.create(request.getUrl()))
                    .timeout(Duration.ofMillis(request.getTimeoutMs()))
                    .POST(HttpRequest.BodyPublishers.ofString(request.getBody(), StandardCharsets.UTF_8));

            applyHeaders(builder, request.getHeaders());

            HttpResponse<InputStream> httpResponse = httpClient.send(builder.build(),
                    HttpResponse.BodyHandlers.ofInputStream());

            UpstreamResponse resp = new UpstreamResponse();
            resp.setStatusCode(httpResponse.statusCode());
            resp.setInputStream(httpResponse.body());
            resp.setStream(true);
            return resp;
        } catch (Exception e) {
            log.error("流式调用上游失败: url={}, error={}", request.getUrl(), e.getMessage());
            UpstreamResponse resp = new UpstreamResponse();
            resp.setStatusCode(0);
            String errorBody = "{\"error\":{\"message\":\"上游连接失败: " + e.getMessage() + "\",\"type\":\"upstream_error\",\"code\":\"upstream_connection_error\"}}";
            resp.setInputStream(new ByteArrayInputStream(errorBody.getBytes(StandardCharsets.UTF_8)));
            resp.setStream(true);
            return resp;
        }
    }

    private void applyHeaders(HttpRequest.Builder builder, Map<String, String> headers) {
        if (headers != null) {
            headers.forEach(builder::header);
        }
    }
}
