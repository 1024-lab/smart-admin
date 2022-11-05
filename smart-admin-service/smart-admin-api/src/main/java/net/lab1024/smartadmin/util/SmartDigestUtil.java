package net.lab1024.smartadmin.util;

import org.apache.commons.codec.digest.DigestUtils;

public class SmartDigestUtil extends DigestUtils {

    /**
     * md5加盐加密
     *
     * @param salt
     * @param password
     * @return
     */
    public static String encryptPassword(String salt, String password) {
        return SmartDigestUtil.md5Hex(String.format(salt, password));
    }
}
