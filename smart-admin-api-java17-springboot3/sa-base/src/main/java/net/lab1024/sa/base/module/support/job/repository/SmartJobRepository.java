package net.lab1024.sa.base.module.support.job.repository;

import net.lab1024.sa.base.module.support.job.repository.domain.SmartJobEntity;
import net.lab1024.sa.base.module.support.job.repository.domain.SmartJobLogEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * job 持久化业务
 *
 * @author huke
 * @date 2024/6/22 22:28
 */
@Service
public class SmartJobRepository {

    @Autowired
    private SmartJobDao jobDao;

    @Autowired
    private SmartJobLogDao jobLogDao;

    public SmartJobDao getJobDao() {
        return jobDao;
    }

    public SmartJobLogDao getJobLogDao() {
        return jobLogDao;
    }

    /**
     * 保存执行记录
     *
     * @param logEntity
     * @param jobEntity
     */
    @Transactional(rollbackFor = Throwable.class)
    public void saveLog(SmartJobLogEntity logEntity, SmartJobEntity jobEntity) {
        jobLogDao.insert(logEntity);

        jobEntity.setLastExecuteLogId(logEntity.getLogId());
        jobDao.updateById(jobEntity);
    }
}
