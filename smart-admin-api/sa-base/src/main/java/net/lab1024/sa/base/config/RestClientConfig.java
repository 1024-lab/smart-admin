package net.lab1024.sa.base.config;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.config.TlsConfig;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.util.TimeValue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestClient;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * http请求配置
 *
 * @Author 1024创新实验室: 卓大
 * @Date 2025-07-26 21:22:12
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Configuration
public class RestClientConfig {

    @Value("${http.pool.max-total}")
    private Integer maxTotal;

    @Value("${http.pool.connect-timeout}")
    private Integer connectTimeout;

    @Value("${http.pool.read-timeout}")
    private Integer readTimeout;

    @Value("${http.pool.write-timeout}")
    private Integer writeTimeout;

    @Value("${http.pool.keep-alive}")
    private Integer keepAlive;

    @Bean
    public RestClient restClient() {

        HttpComponentsClientHttpRequestFactory factory =
                new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(connectTimeout);
        factory.setConnectionRequestTimeout(connectTimeout);
        factory.setReadTimeout(readTimeout);

        PoolingHttpClientConnectionManager cm =
                new PoolingHttpClientConnectionManager();

        cm.setMaxTotal(this.maxTotal);
        cm.setDefaultTlsConfig(TlsConfig.DEFAULT);

        HttpClient httpClient = HttpClients.custom()
                .setConnectionManager(cm)
                .setKeepAliveStrategy((response, context) -> TimeValue.of(this.keepAlive, TimeUnit.MICROSECONDS))
                .build();

        factory.setHttpClient(httpClient);

        return RestClient.builder()
                .requestFactory(factory)
                .messageConverters(converters())
                .build();
    }

    public List<HttpMessageConverter<?>> converters() {
        List<HttpMessageConverter<?>> converters = new ArrayList<>();
        HttpMessageConverter<?> converter = new StringHttpMessageConverter(StandardCharsets.UTF_8);
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        List<MediaType> fastMediaTypes = new ArrayList<>();
        fastMediaTypes.add(MediaType.APPLICATION_FORM_URLENCODED);
        fastMediaTypes.add(MediaType.APPLICATION_JSON);
        fastConverter.setSupportedMediaTypes(fastMediaTypes);
        converters.add(converter);
        converters.add(fastConverter);
        return converters;
    }


}
