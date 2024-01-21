package com.my.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.my.domain.WeChatLoginData;
import com.my.service.WechatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class WeChatLoginController {

    @Autowired
    private WechatService wechatService;

    @PostMapping("/login")
    public String login(@RequestBody WeChatLoginData data) throws JsonProcessingException {
        String code = data.getCode();

        wechatService.login(data);

        // 返回相应的用户信息或token等
        return "Login successful";
    }

}
