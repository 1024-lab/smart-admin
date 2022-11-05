package net.lab1024.sa.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.util.List;

/**
 * yaml 读取配置
 *
 * @Author 1024创新实验室: 罗伊
 * @Date 2022-05-30 21:22:12
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Configuration
@Slf4j
public class PostProcessorConfig implements EnvironmentPostProcessor {

    private final YamlPropertySourceLoader loader = new YamlPropertySourceLoader();

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        MutablePropertySources propertySources = environment.getPropertySources();
        this.loadProperty(propertySources);
    }

    private void loadProperty(MutablePropertySources propertySources) {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            Resource[] resources = resolver.getResources("classpath*:sa-*.yaml");
            if (resources.length < 1) {
                return;
            }
            for (Resource resource : resources) {
                log.info("初始化系统配置：{}", resource.getFilename());
                List<PropertySource<?>> load = loader.load(resource.getFilename(), resource);
                load.forEach(propertySources::addLast);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}