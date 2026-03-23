package net.lab1024.sa.base.module.support.apiencrypt.service;

import cn.hutool.crypto.symmetric.AES;
import cn.hutool.crypto.symmetric.SM4;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.base.common.constant.StringConst;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.security.Security;
import java.util.Base64;

/**
 * AES 加密和解密
 * 1、AES加密算法支持三种密钥长度：128位、192位和256位，这里选择128位
 * 2、AES 要求秘钥为 128bit，转化字节为 16个字节；
 * 3、js前端使用 UCS-2 或者 UTF-16 编码，字母、数字、特殊符号等 占用1个字节；
 * 4、所以：秘钥Key 组成为：字母、数字、特殊符号 一共16个即可
 *
 * @Author 1024创新实验室-主任:卓大
 * @Date 2023/10/21 11:41:46
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */

@Slf4j
public class ApiEncryptServiceAesImpl implements ApiEncryptService {

    private static final String CHARSET = "UTF-8";

    private static final String AES_KEY = "1024lab__1024lab";

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    @Override
    public String encrypt(String data) {
        try {
            //  AES 加密 并转为 base64
            AES aes = new AES(hexToBytes(stringToHex(AES_KEY)));
            return aes.encryptBase64(data);


        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return StringConst.EMPTY;
        }
    }

    @Override
    public String decrypt(String data) {
        try {
            // 第一步： Base64 解码
            byte[] base64Decode = Base64.getDecoder().decode(data);

            // 第二步： AES 解密
            AES aes = new AES(hexToBytes(stringToHex(AES_KEY)));
            byte[] decryptedBytes = aes.decrypt(base64Decode);
            return new String(decryptedBytes, CHARSET);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return StringConst.EMPTY;
        }
    }

    /**
     * 16 进制串转字节数组
     *
     * @param hex 16进制字符串
     * @return byte数组
     */
    public static byte[] hexToBytes(String hex) {
        int length = hex.length();
        byte[] result;
        if (length % 2 == 1) {
            length++;
            result = new byte[(length / 2)];
            hex = "0" + hex;
        } else {
            result = new byte[(length / 2)];
        }
        int j = 0;
        for (int i = 0; i < length; i += 2) {
            result[j] = hexToByte(hex.substring(i, i + 2));
            j++;
        }
        return result;
    }

    public static String stringToHex(String input) {
        char[] chars = input.toCharArray();
        StringBuilder hex = new StringBuilder();
        for (char c : chars) {
            hex.append(Integer.toHexString((int) c));
        }
        return hex.toString();
    }

    /**
     * 16 进制字符转字节
     *
     * @param hex 16进制字符 0x00到0xFF
     * @return byte
     */
    private static byte hexToByte(String hex) {
        return (byte) Integer.parseInt(hex, 16);
    }

}
