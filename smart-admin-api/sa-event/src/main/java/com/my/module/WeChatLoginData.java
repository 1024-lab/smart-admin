package com.my.module;

import lombok.Data;

@Data
public class WeChatLoginData {
    private String code;
    private String encryptedData;
    private String iv;

}
