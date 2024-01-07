package com.my.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.config.WechatConfig;
import com.my.module.WeChatLoginData;
import com.my.module.WeChatSessionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

public class WeChatLoginController {

    @Autowired
    private WechatConfig wechatConfig;

    @PostMapping("/login")
    public String login(@RequestBody WeChatLoginData data) throws JsonProcessingException {
        String code = data.getCode();
        String encryptedData = data.getEncryptedData();
        String iv = data.getIv();


        // 向微信后台发起校验请求
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid="+""+"&secret="+""+"js_code=" + code + "&grant_type=authorization_code";
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);

        // 解析微信后台返回的数据
        ObjectMapper mapper = new ObjectMapper();
        WeChatSessionData sessionData = mapper.readValue(response, WeChatSessionData.class);

        // 获取用户信息或token等
        String openId = sessionData.getOpenid();
        String sessionKey = sessionData.getSession_key();
        // 返回相应的用户信息或token等
        return "Login successful";
    }

}
