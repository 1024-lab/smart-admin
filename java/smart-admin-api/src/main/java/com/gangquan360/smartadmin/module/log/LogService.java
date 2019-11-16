package com.gangquan360.smartadmin.module.log;

import com.gangquan360.smartadmin.module.log.orderoperatelog.OrderOperateLogDao;
import com.gangquan360.smartadmin.module.log.orderoperatelog.domain.entity.OrderOperateLogEntity;
import com.gangquan360.smartadmin.module.log.userloginlog.UserLoginLogDao;
import com.gangquan360.smartadmin.module.log.userloginlog.domain.UserLoginLogEntity;
import com.gangquan360.smartadmin.module.log.useroperatelog.UserOperateLogDao;
import com.gangquan360.smartadmin.module.log.useroperatelog.domain.UserOperateLogEntity;
import com.gangquan360.smartadmin.util.SmartThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * [  ]
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2018 1024lab.netInc. All rights reserved.
 * @date 2019/4/4 0004 下午 16:19
 * @since JDK1.8
 */
@Slf4j
@Service
public class LogService {

    private ThreadPoolExecutor threadPoolExecutor;

    @Autowired
    private UserLoginLogDao userLoginLogDao;

    @Autowired
    private OrderOperateLogDao orderOperateLogDao;

    @Autowired
    private UserOperateLogDao userOperateLogDao;

    @PostConstruct
    void init() {
        if (threadPoolExecutor == null) {
            threadPoolExecutor = new ThreadPoolExecutor(1, 1, 10L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(2000), SmartThreadFactory.create("LogAspect"));
        }
    }

    @PreDestroy
    void destroy() {
        if (threadPoolExecutor != null) {
            threadPoolExecutor.shutdown();
            threadPoolExecutor = null;
        }
    }

    public void addLog(Object object) {
        try {
            if (object instanceof UserLoginLogEntity) {
                threadPoolExecutor.execute(() -> userLoginLogDao.insert((UserLoginLogEntity) object));
            }
            if (object instanceof OrderOperateLogEntity) {
                threadPoolExecutor.execute(() -> orderOperateLogDao.insert((OrderOperateLogEntity) object));
            }
            if (object instanceof UserOperateLogEntity) {
                threadPoolExecutor.execute(() -> userOperateLogDao.insert((UserOperateLogEntity) object));
            }
        } catch (Throwable e) {
            log.error("userLogAfterAdvice:{}", e);
        }
    }
}
