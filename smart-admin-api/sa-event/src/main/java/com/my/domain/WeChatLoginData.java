package com.my.domain;

import lombok.Data;

@Data
public class WeChatLoginData {
    private String code;

    public WeChatLoginData(String code){
        this.code=code;
    }
}
