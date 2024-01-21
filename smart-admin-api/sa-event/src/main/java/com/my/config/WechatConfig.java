package com.my.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class WechatConfig {


    @Value("${wechat.appid}")
    private String appId;
    @Value("${wechat.appsecret}")
    private String appSecret;
    @Value("${wechat.url.code2Session}")
    private String code2SessionUrl;
    @Value("${wechat.url.getPhoneNumber}")
    private String getPhoneNumber;

    @Bean
    public WechatConfig wechatConfig(){
        return new WechatConfig();
    }
}
