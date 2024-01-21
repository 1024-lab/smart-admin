package com.my.util;


import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.AlgorithmParameters;
import java.security.Key;
import java.security.Security;
import java.util.Base64;

public class WechatUtils {
    public static final String AES = "AES";
    public static final String AES_CBC_PADDING = "AES/CBC/PKCS7Padding";
    private static BouncyCastleProvider bouncyCastleProvider = null;
    public static synchronized BouncyCastleProvider getBouncyCastleInstance() {
        if (bouncyCastleProvider == null) {
            bouncyCastleProvider = new BouncyCastleProvider();
        }
        return bouncyCastleProvider;
    }
    /**
     *    * 微信 数据解密<br/>
     *    * 对称解密使用的算法为 AES-128-CBC，数据采用PKCS#7填充<br/>
     *    * 对称解密的目标密文:encrypted=Base64_Decode(encryptData)<br/>
     *    * 对称解密秘钥:key = Base64_Decode(session_key),aeskey是16字节<br/>
     *    * 对称解密算法初始向量:iv = Base64_Decode(iv),同样是16字节<br/>
     *    *
     *    * @param encrypted 目标密文
     *    * @param session_key 会话ID
     *    * @param iv 加密算法的初始向量
     *
     */
    public static String wxDecrypt(String encrypted, String session_key, String iv) {
        final Base64.Decoder decoder = Base64.getDecoder();
        String result = null;
        byte[] encrypted64 = decoder.decode(encrypted);
        byte[] key64 = decoder.decode(session_key);
        byte[] iv64 = decoder.decode(iv);
        try {
            init();
            result = new String(decrypt(encrypted64, key64, generateIV(iv64)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    /**
     *    * 初始化密钥
     *
     */
    public static void init() throws Exception {
        Security.addProvider(getBouncyCastleInstance());
        KeyGenerator.getInstance(AES).init(128);
    }

    /**
     *    * 生成iv
     *
     */
    public static AlgorithmParameters generateIV(byte[] iv) throws Exception {
        // iv 为一个 16 字节的数组，这里采用和 iOS 端一样的构造方法，数据全为0
        // Arrays.fill(iv, (byte) 0x00);
        AlgorithmParameters params = AlgorithmParameters.getInstance(AES);
        params.init(new IvParameterSpec(iv));
        return params;
    }
    /**
     *    * 生成解密
     *
     */
    public static byte[] decrypt(byte[] encryptedData, byte[] keyBytes, AlgorithmParameters iv)
            throws Exception {
        Key key = new SecretKeySpec(keyBytes, AES);
        Cipher cipher = Cipher.getInstance(AES_CBC_PADDING);
        // 设置为解密模式
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        return cipher.doFinal(encryptedData);
    }
}
