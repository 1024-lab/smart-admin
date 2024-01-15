package net.lab1024.sa.base.module.support.securityprotect.service;

import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartStringUtil;
import net.lab1024.sa.base.module.support.apiencrypt.service.ApiEncryptService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 三级等保 密码 相关
 *
 * @Author 1024创新实验室-主任:卓大
 * @Date 2023/10/11 19:25:59
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>，Since 2012
 */

@Service
public class ProtectPasswordService {

    /**
     * 密码长度8-20位且包含大写字母、小写字母、数字三种
     */
    public static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,20}$";

    /**
     * 密码长度8-20位且包含大写字母、小写字母、数字三种
     */
    public static final String PASSWORD_FORMAT_MSG = "密码必须为长度8-20位且包含大写字母、小写字母、数字三种";


    private static final int PASSWORD_LENGTH = 8;


    /**
     * 密码复杂度开启, 默认为true 开启，false 不开启
     */
    @Value("${classified-protect.password-complexity-enabled}")
    private Boolean passwordComplexityEnabled;


    @Resource
    private ApiEncryptService apiEncryptService;

    /**
     * 校验密码复杂度
     *
     * @return
     */
    public ResponseDTO<String> validatePassComplexity(String password) {

        // 无需校验
        if (!passwordComplexityEnabled) {
            return ResponseDTO.ok();
        }

        if (SmartStringUtil.isEmpty(password)) {
            return ResponseDTO.userErrorParam(PASSWORD_FORMAT_MSG);
        }

        if (!password.matches(PASSWORD_PATTERN)) {
            return ResponseDTO.userErrorParam(PASSWORD_FORMAT_MSG);
        }

        return ResponseDTO.ok();
    }

    /**
     * 随机生成密码
     *
     * @return
     */
    public String randomPassword() {
        // 未开启密码复杂度，则由8为数字构成
        if (passwordComplexityEnabled) {
            return RandomStringUtils.randomNumeric(PASSWORD_LENGTH);
        } else {
            // 3位大写字母，2位数字，3位小写字母
            return RandomStringUtils.randomAlphabetic(3).toUpperCase() + RandomStringUtils.randomNumeric(2) + RandomStringUtils.randomAlphabetic(3).toLowerCase();
        }
    }


    /**
     * 解密 SM4 or AES 加密过的密码
     *
     * @param encryptedPassword
     * @return
     */
    public String decryptPassword(String encryptedPassword) {
        return apiEncryptService.decrypt(encryptedPassword);
    }


}
