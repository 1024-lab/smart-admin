package net.lab1024.sa.base.listener;

import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.base.common.util.SmartIpUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.logging.LoggingApplicationListener;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;

/**
 * 初初始化ip工具类
 *
 * @Author 1024创新实验室: zhuoda
 * @Date 2023-09-03 23:45:26
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Order(value = LoggingApplicationListener.DEFAULT_ORDER)
@Slf4j
public class Ip2RegionListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    private static final String IP_FILE_NAME = "ip2region.xdb";

    private static final String LOG_DIRECTORY = "project.log-directory";

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent applicationEvent) {

        ConfigurableEnvironment environment = applicationEvent.getEnvironment();
        String logDirectoryPath = environment.getProperty(LOG_DIRECTORY);
        if (logDirectoryPath == null) {
            throw new ExceptionInInitializerError("环境变量为空：" + LOG_DIRECTORY);
        }
        System.setProperty(LOG_DIRECTORY, logDirectoryPath);

        // 1、从jar中的ip2region.xdb文件复制到服务器目录中
        File logDirectoryFile = new File(logDirectoryPath);
        if (!logDirectoryFile.exists()) {
            logDirectoryFile.mkdirs();
        }

        String tempFilePath = null;
        if (logDirectoryPath.endsWith("/")) {
            tempFilePath = logDirectoryPath + IP_FILE_NAME;
        } else {
            tempFilePath = logDirectoryPath + "/" + IP_FILE_NAME;
        }

        File tempFile = new File(tempFilePath);
        try {
            FileUtils.copyInputStreamToFile(new ClassPathResource(IP_FILE_NAME).getInputStream(), tempFile);

            // 2、初始化
            SmartIpUtil.init(tempFilePath);


        } catch (IOException e) {
            log.error("无法复制ip数据文件 ip2region.xdb", e);
            throw new ExceptionInInitializerError("无法复制ip数据文件");
        } finally {
            if (tempFile.exists()) {
                tempFile.delete();
            }
        }

    }


}