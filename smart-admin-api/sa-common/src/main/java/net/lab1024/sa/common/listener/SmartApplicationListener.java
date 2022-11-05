package net.lab1024.sa.common.listener;

import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.URLUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.context.WebServerApplicationContext;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.boot.web.server.WebServer;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * 启动监听器
 *
 * @Author 1024创新实验室: 罗伊
 * @Date 2021-12-23 23:45:26
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Slf4j
@Component
@Order(value = 1024)
public class SmartApplicationListener implements ApplicationListener<WebServerInitializedEvent> {

    @Override
    public void onApplicationEvent(WebServerInitializedEvent webServerInitializedEvent) {
        WebServer server = webServerInitializedEvent.getWebServer();
        WebServerApplicationContext context = webServerInitializedEvent.getApplicationContext();
        Environment env = context.getEnvironment();
        //获取服务信息
        String ip = NetUtil.getLocalhost().getHostAddress();
        Integer port = server.getPort();
        String contextPath = env.getProperty("server.servlet.context-path");
        if (contextPath == null) {
            contextPath = "";
        }
        String profile = env.getProperty("spring.profiles.active");
        String projectName = env.getProperty("project.name");
        //拼接服务地址
        String title = String.format("-------------【%s】 service is running！current profile is 【%s】-------------", projectName, profile);
        String localhostUrl = URLUtil.normalize(String.format("http://localhost:%d%s", port, contextPath), false, true);
        String externalUrl = URLUtil.normalize(String.format("http://%s:%d%s", ip, port, contextPath), false, true);
        String swaggerUrl = URLUtil.normalize(String.format("http://localhost:%d%s/swagger-ui.html", port, contextPath), false, true);
        log.info("\n{}\n" +
                        "\tLocal:\t\t{}" +
                        "\n\tExternal:\t{}" +
                        "\n\tSwagger:\t{}" +
                        "\n-------------------------------------------------------------------------------------\n",
                title, localhostUrl, externalUrl, swaggerUrl);
    }
}