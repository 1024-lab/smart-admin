package net.lab1024.sa.base.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.config.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 定时任务调度 配置
 *
 * @Author 1024创新实验室: 罗伊
 * @Date 2022-05-30 21:22:12
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Slf4j
@Configuration
public class ScheduleConfig implements SchedulingConfigurer {

    private ScheduledTaskRegistrar taskRegistrar;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        this.taskRegistrar = taskRegistrar;
    }

    public String destroy() {
        List<Task> taskList = new ArrayList<>();
        taskList.addAll(taskRegistrar.getCronTaskList());
        taskList.addAll(taskRegistrar.getTriggerTaskList());
        taskList.addAll(taskRegistrar.getFixedDelayTaskList());
        taskList.addAll(taskRegistrar.getFixedRateTaskList());

        taskRegistrar.destroy();

        List<String> taskNameList = taskList.stream().map(Task::toString).collect(Collectors.toList());
        return "已关闭 @Scheduled定时任务：" + taskNameList.size() + "个！";
    }

}
