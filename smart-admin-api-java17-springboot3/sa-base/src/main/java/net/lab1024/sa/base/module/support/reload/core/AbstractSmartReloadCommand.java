package net.lab1024.sa.base.module.support.reload.core;


import net.lab1024.sa.base.module.support.reload.core.domain.SmartReloadItem;
import net.lab1024.sa.base.module.support.reload.core.domain.SmartReloadObject;
import net.lab1024.sa.base.module.support.reload.core.domain.SmartReloadResult;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 检测是否 Reload 的类
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2015-03-02 19:11:52
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
public abstract class AbstractSmartReloadCommand {

    /**
     * 当前ReloadItem的存储器
     */
    private ConcurrentHashMap<String, String> tagIdentifierMap = new ConcurrentHashMap<>();

    private SmartReloadManager smartReloadManager;

    /**
     * @return
     */
    public void setReloadManager(SmartReloadManager smartReloadManager) {
        this.smartReloadManager = smartReloadManager;
    }

    public void init() {
        List<SmartReloadItem> smartReloadItems = this.readReloadItem();
        if (smartReloadItems != null) {
            for (SmartReloadItem smartReloadItem : smartReloadItems) {
                tagIdentifierMap.put(smartReloadItem.getTag(), smartReloadItem.getIdentification());
            }
        }
    }


    /**
     * 该方法返回一个List<ReloadItem></>:<br>
     * ReloadItem对象的tagIdentify为：该tag的 状态（状态其实就是个字符串，如果该字符串跟上次有变化则进行reload操作）<br>
     * ReloadItem对象的args为： reload操作需要的参数<br><br>
     *
     * @return List<ReloadItem>
     */
    public abstract List<SmartReloadItem> readReloadItem();

    /**
     * 处理Reload结果
     *
     * @param smartReloadResult
     */
    public abstract void handleReloadResult(SmartReloadResult smartReloadResult);


    /**
     * 获取本地缓存tag标识
     *
     * @return
     */
    public ConcurrentHashMap<String, String> getTagIdentifierMap() {
        return tagIdentifierMap;
    }

    /**
     * 设置新的缓存标识
     *
     * @param tag
     * @param identification
     */
    public void putIdentifierMap(String tag, String identification) {
        tagIdentifierMap.put(tag, identification);
    }


    /**
     * 获取重载对象
     *
     * @return
     */
    public SmartReloadObject reloadObject(String tag) {
        if (this.smartReloadManager == null) {
            return null;
        }
        Map<String, SmartReloadObject> reloadObjectMap = smartReloadManager.reloadObjectMap();
        return reloadObjectMap.get(tag);
    }
}
