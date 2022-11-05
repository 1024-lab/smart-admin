package net.lab1024.sa.common.module.support.heartbeat;

import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.common.common.util.SmartBeanUtil;
import net.lab1024.sa.common.module.support.heartbeat.domain.HeartBeatRecordEntity;
import net.lab1024.sa.common.module.support.heartbeat.core.HeartBeatRecord;
import net.lab1024.sa.common.module.support.heartbeat.core.IHeartBeatRecordHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 心跳记录
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-01-09 20:57:24
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Slf4j
@Service
public class HeartBeatRecordHandler implements IHeartBeatRecordHandler {

    @Autowired
    private HeartBeatRecordDao heartBeatRecordDao;

    /**
     * 心跳日志处理方法
     * @param heartBeatRecord
     */
    @Override
    public void handler(HeartBeatRecord heartBeatRecord) {
        HeartBeatRecordEntity heartBeatRecordEntity = SmartBeanUtil.copy(heartBeatRecord, HeartBeatRecordEntity.class);
        HeartBeatRecordEntity heartBeatRecordOld = heartBeatRecordDao.query(heartBeatRecordEntity);
        if (heartBeatRecordOld == null) {
            heartBeatRecordDao.insert(heartBeatRecordEntity);
        } else {
            heartBeatRecordDao.updateHeartBeatTimeById(heartBeatRecordOld.getHeartBeatRecordId(), heartBeatRecordEntity.getHeartBeatTime());
        }
    }

}
