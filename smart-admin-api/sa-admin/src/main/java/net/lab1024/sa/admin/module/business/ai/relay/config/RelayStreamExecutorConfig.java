package net.lab1024.sa.admin.module.business.ai.relay.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Relay 流式转发线程池配置
 */
@Configuration
public class RelayStreamExecutorConfig {

    @Bean("relayStreamExecutor")
    public ExecutorService relayStreamExecutor() {
        return new ThreadPoolExecutor(
                10, 50, 60, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(200),
                r -> {
                    Thread t = new Thread(r, "relay-stream-" + System.nanoTime());
                    t.setDaemon(true);
                    return t;
                },
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }
}
