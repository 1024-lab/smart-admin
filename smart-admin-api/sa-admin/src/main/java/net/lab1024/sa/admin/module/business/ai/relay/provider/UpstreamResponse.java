package net.lab1024.sa.admin.module.business.ai.relay.provider;

import lombok.Data;

import java.io.InputStream;
import java.util.Map;

/**
 * 上游响应封装
 */
@Data
public class UpstreamResponse {

    private int statusCode;

    private Map<String, String> headers;

    /** 非流式时使用 */
    private String body;

    /** 流式时使用 */
    private InputStream inputStream;

    /** 是否为流式响应 */
    private boolean stream;
}
