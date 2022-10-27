package net.lab1024.sa.common.config;

import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.common.module.support.reload.ReloadCommand;
import net.lab1024.sa.common.module.support.reload.core.SmartReloadManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * reload配置
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2021/9/1 21:40
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Slf4j
@Configuration
public class ReloadConfig {

    /**
     * 间隔时间
     */
    @Value("${reload.interval-seconds}")
    private Integer intervalSeconds;

    @Autowired
    private ReloadCommand reloadCommand;

    @Bean
    public SmartReloadManager initSmartReloadManager() {
        // 创建 Reload Manager 调度器
        return new SmartReloadManager(reloadCommand,intervalSeconds);
    }
}
