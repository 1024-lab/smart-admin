package com.gangquan360.smartadmin.module.quartz.service;

import com.gangquan360.smartadmin.module.quartz.dao.QuartzTaskLogDao;
import com.gangquan360.smartadmin.module.quartz.domain.entity.QuartzTaskLogEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * [  ]
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2018 1024lab.netInc. All rights reserved.
 * @date 2019/4/13 0013 下午 14:50
 * @since JDK1.8
 */
@Service
public class QuartzTaskLogService {

    @Autowired
    private QuartzTaskLogDao quartzTaskLogDao;


    public void save(QuartzTaskLogEntity logEntity){
        quartzTaskLogDao.insert(logEntity);
    }
}
