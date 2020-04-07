package net.lab1024.smartadmin.common.reload;

import net.lab1024.smartadmin.common.reload.annotation.SmartReload;
import net.lab1024.smartadmin.common.reload.domain.AbstractSmartReloadObject;
import net.lab1024.smartadmin.common.reload.domain.AnnotationReloadObject;
import net.lab1024.smartadmin.common.reload.domain.InterfaceReloadObject;
import net.lab1024.smartadmin.common.reload.domain.entity.ReloadItem;
import net.lab1024.smartadmin.common.reload.domain.entity.SmartReloadResult;
import net.lab1024.smartadmin.common.reload.interfaces.SmartReloadCommandInterface;
import net.lab1024.smartadmin.common.reload.interfaces.SmartReloadThreadLogger;
import net.lab1024.smartadmin.common.reload.interfaces.SmartReloadable;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.requireNonNull;

/**
 * SmartReloadManager 管理器
 * <p>
 * 可以在此类中添加 检测任务 以及注册 处理程序
 *
 * @author zhuoda
 */
public class SmartReloadManager {

    private Map<String, AbstractSmartReloadObject> tagReloadObject;

    private SmartReloadScheduler reloadScheduler;

    private SmartReloadThreadLogger logger;

    public SmartReloadManager(SmartReloadThreadLogger logger, int threadCount) {
        this.tagReloadObject = new ConcurrentHashMap<>();
        if (logger == null) {
            throw new ExceptionInInitializerError("SmartReloadLoggerImp cannot be null");
        }

        if (threadCount < 1) {
            throw new ExceptionInInitializerError("threadCount must be greater than 1");
        }

        this.logger = logger;
        this.reloadScheduler = new SmartReloadScheduler(this.logger, threadCount);
    }

    /**
     * 默认创建单个线程
     *
     * @param logger
     */
    public SmartReloadManager(SmartReloadThreadLogger logger) {
        this(logger, 1);
    }

    /**
     * 停止
     */
    public void shutdown() {
        reloadScheduler.shutdown();
    }

    /**
     * 添加任务
     *
     * @param command      SmartReloadCommand实现类
     * @param initialDelay 第一次执行前的延迟时间
     * @param delay        任务间隔时间
     * @param unit         延迟单位 TimeUnit 天、小时、分、秒等
     */
    public void addCommand(SmartReloadCommandInterface command, long initialDelay, long delay, TimeUnit unit) {
        reloadScheduler.addCommand(command, initialDelay, delay, unit);
    }

    /**
     * 注册  实现接口的方式
     *
     * @param tag
     * @param reloadable
     */
    public void register(String tag, SmartReloadable reloadable) {
        requireNonNull(reloadable);
        requireNonNull(tag);
        if (tagReloadObject.containsKey(tag)) {
            logger.error("<<SmartReloadManager>> register duplicated tag reload : " + tag + " , and it will be cover!");
        }
        tagReloadObject.put(tag, new InterfaceReloadObject(reloadable));
    }

    /**
     * 注册 要求此类必须包含使用了SmartReload注解的方法
     *
     * @param reloadObject
     */
    public void register(Object reloadObject) {
        requireNonNull(reloadObject);
        Method[] declaredMethods = reloadObject.getClass().getDeclaredMethods();
        if (declaredMethods != null) {
            for (int i = 0; i < declaredMethods.length; i++) {
                Method method = declaredMethods[i];
                SmartReload annotation = method.getAnnotation(SmartReload.class);
                if (annotation != null) {
                    String reloadTag = annotation.value();
                    this.register(reloadTag, new AnnotationReloadObject(reloadObject, method));
                }
            }
        }
    }

    private void register(String tag, AbstractSmartReloadObject reloadObject) {
        if (tagReloadObject.containsKey(tag)) {
            logger.error("<<SmartReloadManager>> register duplicated tag reload : " + tag + " , and it will be cover!");
        }
        tagReloadObject.put(tag, reloadObject);
    }

    /**
     * Reload 已注册的ReloadItem
     *
     * @param reloadItem
     * @return SmartReloadResult
     */
    public SmartReloadResult doReload(ReloadItem reloadItem) {
        AbstractSmartReloadObject reloadObject = tagReloadObject.get(reloadItem.getTag());
        if (reloadObject != null) {
            return reloadObject.reload(reloadItem);
        }
        // 返回注册结果
        return new SmartReloadResult(reloadItem.getTag(), reloadItem.getArgs(), reloadItem.getIdentification(), false, "No registered reload handler was found");
    }

}
