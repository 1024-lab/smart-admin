package net.lab1024.smartadmin.util;

import net.lab1024.smartadmin.module.support.quartz.constant.QuartzConst;
import org.apache.commons.lang3.StringUtils;
import org.quartz.JobKey;
import org.quartz.TriggerKey;

/**
 * [  ]
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2018 1024lab.netInc. All rights reserved.
 * @date 2019/4/13 0013 下午 15:16
 * @since JDK1.8
 */
public class SmartQuartzUtil {

    public static Long getTaskIdByJobKey(JobKey jobKey) {
        String name = jobKey.getName();
        return Long.valueOf(StringUtils.replace(name, QuartzConst.JOB_KEY_PREFIX, ""));
    }

    public static Integer getTaskIdByTriggerKey(TriggerKey triggerKey) {
        String name = triggerKey.getName();
        return Integer.valueOf(StringUtils.replace(name, QuartzConst.TRIGGER_KEY_PREFIX, ""));
    }

    /**
     * 获取触发器key
     */
    public static TriggerKey getTriggerKey(Long taskId) {
        return TriggerKey.triggerKey(QuartzConst.TRIGGER_KEY_PREFIX + taskId);
    }

    /**
     * 获取jobKey
     */
    public static JobKey getJobKey(Long taskId) {
        return JobKey.jobKey(QuartzConst.JOB_KEY_PREFIX + taskId);
    }
}
