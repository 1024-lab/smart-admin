package com.gangquan360.smartadmin.module.quartz.service;

import com.gangquan360.smartadmin.module.quartz.constant.QuartzConst;
import com.gangquan360.smartadmin.module.quartz.constant.TaskResultEnum;
import com.gangquan360.smartadmin.module.quartz.domain.entity.QuartzTaskEntity;
import com.gangquan360.smartadmin.module.quartz.domain.entity.QuartzTaskLogEntity;
import com.gangquan360.smartadmin.module.quartz.task.ITask;
import com.gangquan360.smartadmin.third.SmartApplicationContext;
import com.gangquan360.smartadmin.util.SmartIPUtil;
import com.gangquan360.smartadmin.util.SmartQuartzUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

/**
 * [  ]
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2019 1024lab.netInc. All rights reserved.
 * @date
 * @since JDK1.8
 */
@Slf4j
public class QuartzTask extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        JobDetail jobDetail = context.getJobDetail();
        Object params = jobDetail.getJobDataMap().get(QuartzConst.QUARTZ_PARAMS_KEY);
        JobKey jobKey = jobDetail.getKey();

        Long taskId = SmartQuartzUtil.getTaskIdByJobKey(jobKey);
        QuartzTaskService quartzTaskService = (QuartzTaskService) SmartApplicationContext.getBean("quartzTaskService");
        QuartzTaskEntity quartzTaskEntity = quartzTaskService.getByTaskId(taskId);

        QuartzTaskLogService quartzTaskLogService = (QuartzTaskLogService) SmartApplicationContext.getBean("quartzTaskLogService");

        QuartzTaskLogEntity taskLogEntity = new QuartzTaskLogEntity();
        taskLogEntity.setTaskId(taskId);
        taskLogEntity.setIpAddress(SmartIPUtil.getLocalHostIP());
        taskLogEntity.setTaskName(quartzTaskEntity.getTaskName());
        String paramsStr = null;
        if (params != null) {
            paramsStr = params.toString();
            taskLogEntity.setTaskParams(paramsStr);
        }
        taskLogEntity.setUpdateTime(new Date());
        taskLogEntity.setCreateTime(new Date());
        //任务开始时间
        long startTime = System.currentTimeMillis();
        try {
            ITask taskClass = (ITask) SmartApplicationContext.getBean(quartzTaskEntity.getTaskBean());
            taskClass.execute(paramsStr);
            taskLogEntity.setProcessStatus(TaskResultEnum.SUCCESS.getStatus());
        } catch (Throwable e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw, true);
            e.printStackTrace(pw);
            pw.flush();
            sw.flush();
            taskLogEntity.setProcessStatus(TaskResultEnum.FAIL.getStatus());
            taskLogEntity.setProcessLog(sw.toString());
            log.error("",e);
        } finally {
            long times = System.currentTimeMillis() - startTime;
            taskLogEntity.setProcessDuration(times);
            quartzTaskLogService.save(taskLogEntity);
        }

    }

}
