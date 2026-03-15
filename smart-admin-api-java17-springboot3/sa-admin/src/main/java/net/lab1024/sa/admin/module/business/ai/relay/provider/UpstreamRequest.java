package net.lab1024.sa.admin.module.business.ai.relay.provider;

import lombok.Data;

/**
 * 上游请求封装
 */
@Data
public class UpstreamRequest {

    private String url;

    private String method;

    private java.util.Map<String, String> headers;

    private String body;

    private int timeoutMs;
}
