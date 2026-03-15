package net.lab1024.sa.admin.module.business.ai.relay.service.crypto;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * AES-GCM 实现，密文格式: Base64( iv(12) + ciphertext + tag(16) )
 */
@Slf4j
@Component
public class AesApiKeyCipherImpl implements ApiKeyCipher {

    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int IV_LENGTH = 12;
    private static final int TAG_LENGTH = 128;
    private static final SecureRandom RANDOM = new SecureRandom();

    @Value("${relay.crypto.aes-key:0123456789abcdef0123456789abcdef}")
    private String aesKeyHex;

    @Value("${relay.crypto.kid:local-v1}")
    private String kid;

    @Override
    public String encrypt(String plainApiKey) {
        try {
            byte[] keyBytes = hexToBytes(aesKeyHex);
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
            byte[] iv = new byte[IV_LENGTH];
            RANDOM.nextBytes(iv);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, new GCMParameterSpec(TAG_LENGTH, iv));
            byte[] cipherText = cipher.doFinal(plainApiKey.getBytes(java.nio.charset.StandardCharsets.UTF_8));

            ByteBuffer buffer = ByteBuffer.allocate(iv.length + cipherText.length);
            buffer.put(iv);
            buffer.put(cipherText);
            return Base64.getEncoder().encodeToString(buffer.array());
        } catch (Exception e) {
            throw new RuntimeException("API Key加密失败", e);
        }
    }

    @Override
    public String decrypt(String cipherApiKey, String kid) {
        try {
            byte[] decoded = Base64.getDecoder().decode(cipherApiKey);
            byte[] iv = new byte[IV_LENGTH];
            byte[] cipherText = new byte[decoded.length - IV_LENGTH];
            System.arraycopy(decoded, 0, iv, 0, IV_LENGTH);
            System.arraycopy(decoded, IV_LENGTH, cipherText, 0, cipherText.length);

            byte[] keyBytes = hexToBytes(aesKeyHex);
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, new GCMParameterSpec(TAG_LENGTH, iv));
            byte[] plain = cipher.doFinal(cipherText);
            return new String(plain, java.nio.charset.StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("API Key解密失败", e);
        }
    }

    @Override
    public String currentKid() {
        return kid;
    }

    @Override
    public String mask(String plainApiKey) {
        if (plainApiKey == null || plainApiKey.length() <= 8) {
            return "****";
        }
        String prefix = plainApiKey.substring(0, 4);
        String suffix = plainApiKey.substring(plainApiKey.length() - 4);
        return prefix + "****" + suffix;
    }

    private byte[] hexToBytes(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }
}
