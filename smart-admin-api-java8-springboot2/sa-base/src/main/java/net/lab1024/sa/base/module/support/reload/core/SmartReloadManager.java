package net.lab1024.sa.base.module.support.reload.core;


import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.base.module.support.reload.core.annoation.SmartReload;
import net.lab1024.sa.base.module.support.reload.core.domain.SmartReloadObject;
import net.lab1024.sa.base.module.support.reload.core.thread.SmartReloadRunnable;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.lang.reflect.Method;
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
public class SmartReloadManager implements BeanPostProcessor {

    private static final String THREAD_NAME_PREFIX = "smart-reload";
    private static final int THREAD_COUNT = 1;

    @Value("${reload.interval-seconds}")
    private Integer intervalSeconds;

    @Resource
    private AbstractSmartReloadCommand reloadCommand;

    private final Map<String, SmartReloadObject> reloadObjectMap = new ConcurrentHashMap<>();

    private ScheduledThreadPoolExecutor threadPoolExecutor;


    @PostConstruct
    public void init() {
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
        this.reloadCommand.setReloadManager(this);
    }


    @PreDestroy
    public void shutdown() {
        if (this.threadPoolExecutor != null) {
            this.threadPoolExecutor.shutdownNow();
            this.threadPoolExecutor = null;
        }
    }


    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Method[] methods = ReflectionUtils.getAllDeclaredMethods(bean.getClass());
        for (Method method : methods) {
            SmartReload smartReload = method.getAnnotation(SmartReload.class);
            if (smartReload == null) {
                continue;
            }
            int paramCount = method.getParameterCount();
            if (paramCount > 1) {
                log.error("<<SmartReloadManager>> register tag reload : " + smartReload.value() + " , param count cannot greater than one !");
                continue;
            }
            String reloadTag = smartReload.value();
            this.register(reloadTag, new SmartReloadObject(bean, method));
        }
        return bean;
    }

    /**
     * 注册reload
     *
     * @param tag
     * @param smartReloadObject
     */
    private void register(String tag, SmartReloadObject smartReloadObject) {
        if (reloadObjectMap.containsKey(tag)) {
            log.error("<<SmartReloadManager>> register duplicated tag reload : " + tag + " , and it will be cover!");
        }
        reloadObjectMap.put(tag, smartReloadObject);
    }

    /**
     * 获取重载对象
     *
     * @return
     */
    public Map<String, SmartReloadObject> reloadObjectMap() {
        return this.reloadObjectMap;
    }


}
