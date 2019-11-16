package com.gangquan360.smartadmin.module.smartreload;

import com.gangquan360.smartadmin.common.constant.ResponseCodeConst;
import com.gangquan360.smartadmin.common.domain.ResponseDTO;
import com.gangquan360.smartadmin.common.reload.SmartReloadManager;
import com.gangquan360.smartadmin.module.smartreload.dao.ReloadItemDao;
import com.gangquan360.smartadmin.module.smartreload.dao.ReloadResultDao;
import com.gangquan360.smartadmin.module.smartreload.domain.dto.ReloadItemUpdateDTO;
import com.gangquan360.smartadmin.module.smartreload.domain.dto.ReloadItemVO;
import com.gangquan360.smartadmin.module.smartreload.domain.dto.ReloadResultVO;
import com.gangquan360.smartadmin.module.smartreload.domain.entity.ReloadItemEntity;
import com.gangquan360.smartadmin.module.smartreload.domain.entity.ReloadResultEntity;
import com.gangquan360.smartadmin.util.SmartBeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Smart initDefines 业务
 *
 * @author listen
 * @date 2018/02/10 13:49
 */
@Service
public class SmartReloadService {

    @Autowired
    private SmartReloadManager smartReloadManager;

    @Autowired
    private SmartReloadCommand smartReloadCommand;

    @Autowired
    private ReloadItemDao reloadItemDao;

    @Autowired
    private ReloadResultDao reloadResultDao;

    @Value("${smart-reload.time-interval}")
    private Long timeInterval;

    @PostConstruct
    public void init() {
        smartReloadManager.addCommand(smartReloadCommand, 10, timeInterval, TimeUnit.SECONDS);
    }

    /**
     * 注册到SmartReload里
     */
    public void register(Object reload) {
        smartReloadManager.register(reload);
    }

    /**
     * 获取所有 initDefines 项
     *
     * @return
     */
    public ResponseDTO<List<ReloadItemVO>> listAllReloadItem() {
        List<ReloadItemEntity> reloadItemEntityList = reloadItemDao.selectList(null);
        List<ReloadItemVO> reloadItemDTOList = SmartBeanUtil.copyList(reloadItemEntityList, ReloadItemVO.class);
        return ResponseDTO.succData(reloadItemDTOList);
    }

    /**
     * 根据 tag
     * 获取所有 initDefines 运行结果
     *
     * @return ResponseDTO
     */
    public ResponseDTO<List<ReloadResultVO>> listReloadItemResult(String tag) {
        ReloadResultEntity query = new ReloadResultEntity();
        query.setTag(tag);
        List<ReloadResultEntity> reloadResultEntityList = reloadResultDao.query(tag);
        List<ReloadResultVO> reloadResultDTOList = SmartBeanUtil.copyList(reloadResultEntityList, ReloadResultVO.class);
        return ResponseDTO.succData(reloadResultDTOList);
    }

    /**
     * 通过标签更新标识符
     *
     * @param updateDTO
     * @return
     */
    public ResponseDTO<String> updateByTag(ReloadItemUpdateDTO updateDTO) {
        ReloadItemEntity entity = new ReloadItemEntity();
        entity.setTag(updateDTO.getTag());
        ReloadItemEntity reloadItemEntity = reloadItemDao.selectById(entity.getTag());
        if (null == reloadItemEntity) {
            return ResponseDTO.wrap(ResponseCodeConst.NOT_EXISTS);
        }
        reloadItemEntity.setIdentification(updateDTO.getIdentification());
        reloadItemEntity.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        reloadItemEntity.setArgs(updateDTO.getArgs());
        reloadItemDao.updateById(reloadItemEntity);
        return ResponseDTO.succ();
    }
}
