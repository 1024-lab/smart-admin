package net.lab1024.sa.base.listener;

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.logging.LoggingApplicationListener;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * 将application.yam l中的日志路径变量:project.log-path注入到 log4j2.xml
 *
 * @Author 1024创新实验室: zhuoda
 * @Date 2023-09-03 23:45:26
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Order(value = LoggingApplicationListener.DEFAULT_ORDER - 1)
public class LogVariableListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    private static final String LOG_DIRECTORY = "project.log-directory";

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent applicationEvent) {

        ConfigurableEnvironment environment = applicationEvent.getEnvironment();
        String filePath = environment.getProperty(LOG_DIRECTORY);
        if (filePath != null) {
            System.setProperty(LOG_DIRECTORY, filePath);
        }
    }
}