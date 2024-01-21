package com.my.domain;

import lombok.Data;

@Data
public class WeChatLoginVo {
    private String code;

    public WeChatLoginVo(String code){
        this.code=code;
    }
}
