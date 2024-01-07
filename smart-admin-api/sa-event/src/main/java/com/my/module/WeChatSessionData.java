package com.my.module;

import lombok.Data;

@Data
public class WeChatSessionData {
    private String openid;
    private String session_key;
    private String unionid;
    private String errmsg;
    private int errcode;
}
