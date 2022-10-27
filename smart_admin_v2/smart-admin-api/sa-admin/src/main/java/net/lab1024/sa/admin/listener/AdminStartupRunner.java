package net.lab1024.sa.admin.listener;

import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.common.common.code.ErrorCodeRegister;
import net.lab1024.sa.common.config.ScheduleConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * admin 应用启动加载
 *
 * @Author 1024创新实验室-主任:卓大
 * @Date 2021-08-26 18:46:32
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ），2012-2022
 */
@Slf4j
@Component
public class AdminStartupRunner implements CommandLineRunner {

    @Autowired
    private ScheduleConfig scheduleConfig;

    @Override
    public void run(String... args) {

        // 初始化状态码
        int codeCount = ErrorCodeRegister.initialize();

        //TODO <卓大> ：根据实际情况来决定是否开启定时任务
        String destroySchedules = "Spring 定时任务 @Schedule 已启动";
//        destroySchedules = scheduleConfig.destroy();

        log.info("\n ---------------【1024创新实验室 温馨提示：】 ErrorCode 共计完成初始化： {}个！---------------" +
                 "\n ---------------【1024创新实验室 温馨提示：】 {}---------------\n", codeCount, destroySchedules);

    }
}