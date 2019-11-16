package com.gangquan360.smartadmin.module.smartreload;

import com.gangquan360.smartadmin.common.reload.abstracts.AbstractSmartReloadCommand4Spring;
import com.gangquan360.smartadmin.common.reload.domain.entity.ReloadItem;
import com.gangquan360.smartadmin.common.reload.domain.entity.SmartReloadResult;
import com.gangquan360.smartadmin.module.smartreload.dao.ReloadItemDao;
import com.gangquan360.smartadmin.module.smartreload.dao.ReloadResultDao;
import com.gangquan360.smartadmin.module.smartreload.domain.entity.ReloadItemEntity;
import com.gangquan360.smartadmin.module.smartreload.domain.entity.ReloadResultEntity;
import com.gangquan360.smartadmin.util.SmartBeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Smart Reload 业务
 *
 * @author listen
 * @date 2018/02/10 09:18
 */
@Component
public class SmartReloadCommand extends AbstractSmartReloadCommand4Spring {

    @Autowired
    private ReloadItemDao reloadItemDao;

    @Autowired
    private ReloadResultDao reloadResultDao;

    /**
     * 读取数据库中SmartReload项
     *
     * @return List<ReloadItem>
     */
    @Override
    public List<ReloadItem> readReloadItem() {
        List<ReloadItemEntity> reloadItemEntityList = reloadItemDao.selectList(null);
        return SmartBeanUtil.copyList(reloadItemEntityList, ReloadItem.class);
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
