package net.lab1024.sa.base.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 异步调用线程配置
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2021-09-02 20:21:10
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Slf4j
@Configuration
public class AsyncConfig {

    /**
     * 线程池 配置bean名称
     */
    public static final String ASYNC_EXECUTOR_THREAD_NAME = "smart-async-executor";

    /**
     * 配置线程池
     *
     * @return
     */
    @Bean(name = ASYNC_EXECUTOR_THREAD_NAME)
    public AsyncTaskExecutor executor() {
        int processors = Runtime.getRuntime().availableProcessors();
        int threadCount = Math.max(1, processors - 1);
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        // 核心线程数量
        taskExecutor.setCorePoolSize(threadCount);
        // 最大线程数量
        taskExecutor.setMaxPoolSize(threadCount);
        taskExecutor.setThreadNamePrefix(ASYNC_EXECUTOR_THREAD_NAME);
        taskExecutor.initialize();
        return taskExecutor;
    }

    /**
     * spring 异步任务 异常配置
     */
    @Configuration
    public static class AsyncExceptionConfig implements AsyncConfigurer {
        @Override
        public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
            return new AsyncExceptionHandler();
        }
    }

    /**
     * 自定义异常处理
     */
    public static class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {
        @Override
        public void handleUncaughtException(Throwable throwable, Method method, Object... objects) {
            log.error("异步任务发生异常:{}, 参数:{}, ", method.getDeclaringClass().getSimpleName() + "." + method.getName(), Arrays.toString(objects), throwable);
        }
    }
}
