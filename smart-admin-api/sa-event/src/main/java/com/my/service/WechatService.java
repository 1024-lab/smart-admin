package com.my.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.my.config.WechatConfig;
import com.my.domain.WeChatLoginData;
import com.my.domain.WeChatSessionData;
import net.lab1024.sa.common.common.domain.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class WechatService {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private WechatConfig wechatConfig;

    public ResponseDTO<WeChatSessionData> login(WeChatLoginData loginData) throws JsonProcessingException {
        // 向微信后台发起校验请求
        String loginUrl = wechatConfig.getCode2SessionUrl();
        String appid=wechatConfig.getAppId();
        String appSecret= wechatConfig.getAppSecret();
        loginUrl=loginUrl+"?appid={appid}}&secret={secret}&js_code={code}&grant_type={grant_type}}";
        Map<String, String> requestUrlParam = new HashMap<>();
        // 小程序appId，自己补充
        requestUrlParam.put("appid", wechatConfig.getAppId());
        // 小程序secret，自己补充
        requestUrlParam.put("secret", wechatConfig.getAppSecret());
        // 小程序端返回的code
        requestUrlParam.put("js_code", loginData.getCode());
        // 默认参数
        requestUrlParam.put("grant_type", "authorization_code");

        WeChatSessionData sessionData = restTemplate.getForObject(loginUrl, WeChatSessionData.class, requestUrlParam);
        /*
        * 查找用户是否存在，不存在则存入
        * 存在则返回用户信息
        * */


        return ResponseDTO.ok(sessionData);
    }

}
