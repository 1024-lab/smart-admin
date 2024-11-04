package net.lab1024.sa.base.module.support.job.core;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.base.module.support.job.config.SmartJobConfig;
import net.lab1024.sa.base.module.support.job.constant.SmartJobConst;
import net.lab1024.sa.base.module.support.job.constant.SmartJobUtil;
import net.lab1024.sa.base.module.support.job.repository.SmartJobRepository;
import net.lab1024.sa.base.module.support.job.repository.domain.SmartJobEntity;
import org.redisson.api.RedissonClient;
import org.springframework.util.CollectionUtils;

import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 定时任务 作业启动类
 *
 * @author huke
 * @date 2024/6/17 21:30
 */
@Slf4j
public class SmartJobLauncher {

    private final SmartJobRepository jobRepository;

    private final List<SmartJob> jobInterfaceList;

    private final RedissonClient redissonClient;

    public SmartJobLauncher(SmartJobConfig jobConfig,
                            SmartJobRepository jobRepository,
                            List<SmartJob> jobInterfaceList,
                            RedissonClient redissonClient) {
        this.jobRepository = jobRepository;
        this.jobInterfaceList = jobInterfaceList;
        this.redissonClient = redissonClient;

        // init job scheduler
        SmartJobScheduler.init(jobConfig);

        // 任务自动检测配置 固定1个线程
        Integer initDelay = jobConfig.getInitDelay();
        Boolean refreshEnabled = jobConfig.getDbRefreshEnabled();
        Integer refreshInterval = jobConfig.getDbRefreshInterval();

        ThreadFactory factory = new ThreadFactoryBuilder().setNameFormat("SmartJobLauncher-%d").build();
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1, factory);
        Runnable launcherRunnable = () -> {
            try {
                // 查询所有任务
                List<SmartJobEntity> smartJobList = this.queryJob();
                this.startOrRefreshJob(smartJobList);
            } catch (Throwable t) {
                log.error("SmartJob Error:", t);
            }
            // 只在启动时 执行一次
            if (!refreshEnabled) {
                executor.shutdown();
            }
        };
        executor.scheduleWithFixedDelay(launcherRunnable, initDelay, refreshInterval, TimeUnit.SECONDS);

        // 打印信息
        String refreshDesc = refreshEnabled ? "开启|检测间隔" + refreshInterval + "秒" : "关闭";
        String format = String.format(SmartJobConst.LOGO, jobConfig.getCorePoolSize(), initDelay, refreshDesc);
        SmartJobUtil.printInfo(format);
    }

    /**
     * 查询数据库
     * 启动/刷新任务
     */
    public void startOrRefreshJob(List<SmartJobEntity> smartJobList) {
        // 查询任务配置
        if (CollectionUtils.isEmpty(smartJobList) || CollectionUtils.isEmpty(jobInterfaceList)) {
            log.info("==== SmartJob ==== job list empty");
            return;
        }

        // 任务实现类
        Map<String, SmartJob> jobImplMap = jobInterfaceList.stream().collect(Collectors.toMap(SmartJob::getClassName, Function.identity()));
        for (SmartJobEntity jobEntity : smartJobList) {
            // 任务是否存在 判断是否需要更新
            Integer jobId = jobEntity.getJobId();
            SmartJobEntity oldJobEntity = SmartJobScheduler.getJobInfo(jobId);
            if (null != oldJobEntity) {
                // 不需要更新
                if (!isNeedUpdate(oldJobEntity, jobEntity)) {
                    continue;
                }
                // 需要更新 移除原任务
                SmartJobScheduler.removeJob(jobId);
            }
            // 任务未开启
            if (!jobEntity.getEnabledFlag()) {
                continue;
            }
            // 查找任务实现类
            SmartJob jobImpl = jobImplMap.get(jobEntity.getJobClass());
            if (null == jobImpl) {
                continue;
            }
            // 添加任务
            SmartJobExecutor jobExecute = new SmartJobExecutor(jobEntity, jobRepository, jobImpl, redissonClient);
            SmartJobScheduler.addJob(jobExecute);
        }
        List<SmartJobEntity> runjJobList = SmartJobScheduler.getJobInfo();
        List<String> jobNameList = runjJobList.stream().map(SmartJobEntity::getJobName).collect(Collectors.toList());
        log.info("==== SmartJob ==== start/refresh job num:{}->{}", runjJobList.size(), jobNameList);
    }

    /**
     * 查询全部任务
     *
     * @return
     */
    private List<SmartJobEntity> queryJob() {
        return jobRepository.getJobDao().selectList(null);
    }

    /**
     * 手动判断 任务配置 是否需要更新
     * 新增字段的话 在这个方法里增加判断
     *
     * @return
     */
    private static boolean isNeedUpdate(SmartJobEntity oldJob, SmartJobEntity newJob) {
        // cron为空时 fixedDelay 才有意义
        return !Objects.equals(oldJob.getEnabledFlag(), newJob.getEnabledFlag())
                || !Objects.equals(oldJob.getTriggerType(), newJob.getTriggerType())
                || !Objects.equals(oldJob.getTriggerValue(), newJob.getTriggerValue())
                || !Objects.equals(oldJob.getJobClass(), newJob.getJobClass());
    }

    @PreDestroy
    public void destroy() {
        SmartJobScheduler.destroy();
        log.info("==== SmartJob ==== destroy job");
    }
}
