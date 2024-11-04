package net.lab1024.sa.base.module.support.reload;

import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.module.support.reload.core.AbstractSmartReloadCommand;
import net.lab1024.sa.base.module.support.reload.core.domain.SmartReloadItem;
import net.lab1024.sa.base.module.support.reload.core.domain.SmartReloadResult;
import net.lab1024.sa.base.module.support.reload.dao.ReloadItemDao;
import net.lab1024.sa.base.module.support.reload.dao.ReloadResultDao;
import net.lab1024.sa.base.module.support.reload.domain.ReloadItemEntity;
import net.lab1024.sa.base.module.support.reload.domain.ReloadResultEntity;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * reload 操作
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2015-03-02 19:11:52
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Component
public class ReloadCommand extends AbstractSmartReloadCommand {

    @Resource
    private ReloadItemDao reloadItemDao;

    @Resource
    private ReloadResultDao reloadResultDao;

    /**
     * 读取数据库中SmartReload项
     *
     * @return List<ReloadItem>
     */
    @Override
    public List<SmartReloadItem> readReloadItem() {
        List<ReloadItemEntity> reloadItemEntityList = reloadItemDao.selectList(null);
        return SmartBeanUtil.copyList(reloadItemEntityList, SmartReloadItem.class);
    }


    /**
     * 保存reload结果
     *
     * @param smartReloadResult
     */
    @Override
    public void handleReloadResult(SmartReloadResult smartReloadResult) {
        ReloadResultEntity reloadResultEntity = SmartBeanUtil.copy(smartReloadResult, ReloadResultEntity.class);
        reloadResultDao.insert(reloadResultEntity);
    }
}
