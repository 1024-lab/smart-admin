package com.my.business;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.config.WechatConfig;
import com.my.module.WeChatLoginData;
import com.my.module.WeChatSessionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class WechatBusiness {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private WechatConfig wechatConfig;

    public WeChatSessionData getSessionKeyOrOpenId(WeChatLoginData loginData) throws JsonProcessingException {
        // 向微信后台发起校验请求
        String loginUrl = wechatConfig.getLoginUrl();
        String appid=wechatConfig.getAppId();
        String appSecret= wechatConfig.getAppSecret();
         //       "https://api.weixin.qq.com/sns/jscode2session?appid="+""+"&secret="+""+"js_code=" + code + "&grant_type=authorization_code";

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(loginUrl, String.class);

        // 解析微信后台返回的数据
        ObjectMapper mapper = new ObjectMapper();
        WeChatSessionData sessionData = mapper.readValue(response, WeChatSessionData.class);
        return sessionData;
    }

}
