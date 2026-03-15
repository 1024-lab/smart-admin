package net.lab1024.sa.admin.module.business.ai.relay.service.crypto;

/**
 * 渠道 API Key 加解密接口，支持密钥轮换 (kid)
 */
public interface ApiKeyCipher {

    /**
     * 加密明文 API Key
     *
     * @param plainApiKey 明文
     * @return 密文
     */
    String encrypt(String plainApiKey);

    /**
     * 解密密文 API Key
     *
     * @param cipherApiKey 密文
     * @param kid          密钥标识
     * @return 明文
     */
    String decrypt(String cipherApiKey, String kid);

    /**
     * 当前使用的密钥标识
     */
    String currentKid();

    /**
     * 生成掩码，如 sk-****abcd
     */
    String mask(String plainApiKey);
}
