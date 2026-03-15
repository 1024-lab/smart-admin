package net.lab1024.sa.admin.module.business.ai.relay.upstream;

import net.lab1024.sa.admin.module.business.ai.relay.provider.UpstreamRequest;
import net.lab1024.sa.admin.module.business.ai.relay.provider.UpstreamResponse;

/**
 * 上游调用接口
 */
public interface UpstreamCaller {

    /**
     * 非流式调用上游
     */
    UpstreamResponse call(UpstreamRequest request);

    /**
     * 流式调用上游（返回 InputStream 用于 SSE 透传）
     */
    UpstreamResponse callStream(UpstreamRequest request);
}
