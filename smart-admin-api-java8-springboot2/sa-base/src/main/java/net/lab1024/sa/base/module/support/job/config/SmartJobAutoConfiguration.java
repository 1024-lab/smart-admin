package net.lab1024.sa.base.module.support.job.config;

import net.lab1024.sa.base.module.support.job.core.SmartJob;
import net.lab1024.sa.base.module.support.job.core.SmartJobLauncher;
import net.lab1024.sa.base.module.support.job.repository.SmartJobRepository;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 定时任务 配置
 *
 * @author huke
 * @date 2024/6/17 21:30
 */
@Configuration
@EnableConfigurationProperties(SmartJobConfig.class)
@ConditionalOnProperty(
        prefix = SmartJobConfig.CONFIG_PREFIX,
        name = "enabled",
        havingValue = "true"
)
public class SmartJobAutoConfiguration {

    private final SmartJobConfig jobConfig;

    private final SmartJobRepository jobRepository;

    private final List<SmartJob> jobInterfaceList;

    public SmartJobAutoConfiguration(SmartJobConfig jobConfig,
                                     SmartJobRepository jobRepository,
                                     List<SmartJob> jobInterfaceList) {
        this.jobConfig = jobConfig;
        this.jobRepository = jobRepository;
        this.jobInterfaceList = jobInterfaceList;
    }

    /**
     * 定时任务启动器
     *
     * @return
     */
    @Bean
    public SmartJobLauncher initJobLauncher(RedissonClient redissonClient) {
        return new SmartJobLauncher(jobConfig, jobRepository, jobInterfaceList, redissonClient);
    }
}
