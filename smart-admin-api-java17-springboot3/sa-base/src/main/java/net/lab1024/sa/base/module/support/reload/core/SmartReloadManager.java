package net.lab1024.sa.base.module.support.reload.core;


import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.base.module.support.reload.core.annoation.SmartReload;
import net.lab1024.sa.base.module.support.reload.core.domain.SmartReloadObject;
import net.lab1024.sa.base.module.support.reload.core.thread.SmartReloadRunnable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * SmartReloadManager 管理器
 * <p>
 * 可以在此类中添加 检测任务 以及注册 处理程序
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2015-03-02 19:11:52
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Slf4j
@Service
public class SmartReloadManager implements ApplicationListener<ContextRefreshedEvent> {

    @Value("${reload.interval-seconds}")
    private Integer intervalSeconds;

    @Resource
    private AbstractSmartReloadCommand reloadCommand;

    private final static Map<String, SmartReloadObject> RELOAD_OBJECT_MAP = new ConcurrentHashMap<>();

    private ScheduledThreadPoolExecutor threadPoolExecutor;

    private static final String THREAD_NAME_PREFIX = "smart-reload";

    private static final int THREAD_COUNT = 1;

    private ApplicationContext applicationContext;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        this.applicationContext = event.getApplicationContext();

        // 初始化
        this.init();
    }

    public void init() {
        this.registerReloadBean();

        if (threadPoolExecutor != null) {
            return;
        }

        this.threadPoolExecutor = new ScheduledThreadPoolExecutor(THREAD_COUNT, r -> {
            Thread t = new Thread(r, THREAD_NAME_PREFIX);
            if (!t.isDaemon()) {
                t.setDaemon(true);
            }
            return t;
        });
        this.threadPoolExecutor.scheduleWithFixedDelay(new SmartReloadRunnable(this.reloadCommand), 10, this.intervalSeconds, TimeUnit.SECONDS);
    }

    @PreDestroy
    public void shutdown() {
        if (this.threadPoolExecutor != null) {
            this.threadPoolExecutor.shutdownNow();
            this.threadPoolExecutor = null;
        }
    }

    /**
     * 注册 reload bean
     */
    public void registerReloadBean() {
        // 遍历所有Bean
        String[] beanNames = applicationContext.getBeanDefinitionNames();
        for (String beanName : beanNames) {
            Object bean = applicationContext.getBean(beanName);
            ReflectionUtils.doWithMethods(bean.getClass(), method -> {
                SmartReload smartReload = method.getAnnotation(SmartReload.class);
                if (smartReload == null) {
                    return;
                }
                int paramCount = method.getParameterCount();
                if (paramCount > 1) {
                    log.error("<<SmartReloadManager>> register tag reload : " + smartReload.value() + " , param count cannot greater than one !");
                    return;
                }
                String reloadTag = smartReload.value();
                this.register(reloadTag, new SmartReloadObject(bean, method));
            });
        }
    }

    /**
     * 注册reload
     *
     * @param tag
     * @param smartReloadObject
     */
    private void register(String tag, SmartReloadObject smartReloadObject) {
        if (RELOAD_OBJECT_MAP.containsKey(tag)) {
            log.error("<<SmartReloadManager>> register duplicated tag reload : " + tag + " , and it will be cover!");
        }
        RELOAD_OBJECT_MAP.put(tag, smartReloadObject);
    }

    /**
     * 获取重载对象
     *
     * @return
     */
    public static SmartReloadObject getReloadObject(String tag) {
        return RELOAD_OBJECT_MAP.get(tag);
    }
}
