package net.lab1024.sa.base.module.support.heartbeat.core;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 心跳核心调度管理器
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-01-09 20:57:24
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
public class HeartBeatManager {

    private static final String THREAD_NAME_PREFIX = "smart-heart-beat";
    private static final int THREAD_COUNT = 1;
    private static final long INITIAL_DELAY = 60 * 1000L;

    private ScheduledThreadPoolExecutor threadPoolExecutor;

    /**
     * 服务状态持久化处理类
     */
    private IHeartBeatRecordHandler heartBeatRecordHandler;

    /**
     * 调度配置信息
     */
    private long intervalMilliseconds;

    /**
     * @param intervalMilliseconds 间隔执行时间(毫秒)
     */
    public HeartBeatManager(Long intervalMilliseconds,
                            IHeartBeatRecordHandler heartBeatRecordHandler) {
        this.intervalMilliseconds = intervalMilliseconds;
        this.heartBeatRecordHandler = heartBeatRecordHandler;
        //使用守护线程去处理
        this.threadPoolExecutor = new ScheduledThreadPoolExecutor(THREAD_COUNT, r -> {
            Thread t = new Thread(r, THREAD_NAME_PREFIX);
            if (!t.isDaemon()) {
                t.setDaemon(true);
            }
            return t;
        });
        // 开始心跳
        this.beginHeartBeat();
    }

    /**
     * 开启心跳
     */
    private void beginHeartBeat() {
        HeartBeatRunnable heartBeatRunnable = new HeartBeatRunnable(heartBeatRecordHandler);
        threadPoolExecutor.scheduleWithFixedDelay(heartBeatRunnable, INITIAL_DELAY, intervalMilliseconds, TimeUnit.MILLISECONDS);
    }
}
