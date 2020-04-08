package net.lab1024.smartadmin.common.reload;

import net.lab1024.smartadmin.common.reload.interfaces.SmartReloadCommandInterface;
import net.lab1024.smartadmin.common.reload.interfaces.SmartReloadThreadLogger;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Reload 调度器
 *
 * @author zhuoda
 */
public class SmartReloadScheduler {

    private ScheduledThreadPoolExecutor executor;

    private SmartReloadThreadLogger logger;

    SmartReloadScheduler(SmartReloadThreadLogger logger, int threadCount) {
        this.executor = new ScheduledThreadPoolExecutor(threadCount, new SmartReloadSchedulerThreadFactory());
        this.logger = logger;
    }

    void shutdown() {
        try {
            executor.shutdown();
        } catch (Throwable e) {
            logger.error("<<SmartReloadScheduler>> shutdown ", e);
        }
    }

    void addCommand(SmartReloadCommandInterface command, long initialDelay, long delay, TimeUnit unit) {
        executor.scheduleWithFixedDelay(new ScheduleRunnable(command, this.logger), initialDelay, delay, unit);
    }

    static class ScheduleRunnable implements Runnable {

        private SmartReloadCommandInterface command;

        private SmartReloadThreadLogger logger;

        public ScheduleRunnable(SmartReloadCommandInterface command, SmartReloadThreadLogger logger) {
            this.command = command;
            this.logger = logger;
        }

        @Override
        public void run() {
            try {
                command.doTask();
            } catch (Throwable e) {
                logger.error("", e);
            }
        }
    }

    static class SmartReloadSchedulerThreadFactory implements ThreadFactory {

        private static final AtomicInteger poolNumber = new AtomicInteger(1);

        private final ThreadGroup group;

        private final AtomicInteger threadNumber = new AtomicInteger(1);

        private final String namePrefix;

        SmartReloadSchedulerThreadFactory() {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
            namePrefix = "smartreload-" + poolNumber.getAndIncrement() + "-thread-";
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
            if (t.isDaemon())
                t.setDaemon(false);
            if (t.getPriority() != Thread.NORM_PRIORITY)
                t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }
    }

}
