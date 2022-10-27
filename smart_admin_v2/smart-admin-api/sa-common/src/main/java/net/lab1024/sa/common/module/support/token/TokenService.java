package net.lab1024.sa.common.module.support.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.common.common.enumeration.UserTypeEnum;
import net.lab1024.sa.common.constant.RedisKeyConst;
import net.lab1024.sa.common.module.support.redis.RedisService;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * 与用户token的相关的服务
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2021-11-29 19:48:35
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Component
@Slf4j
public class TokenService {
    private static final long HOUR_TIME_MILLI = 60 * 60 * 1000;

    @Value("${token.key}")
    private String tokenKey;

    @Value("${token.expire-day}")
    private Integer tokenExpire;

    @Autowired
    private RedisService redisService;

    /**
     * 生成Token，并存入redis
     *
     * @param userId
     * @param userName
     * @param userTypeEnum
     * @param loginDeviceEnum
     * @param superPasswordFlag 特殊万能密码标识
     * @return
     */
    public String generateToken(Long userId, String userName, UserTypeEnum userTypeEnum, LoginDeviceEnum loginDeviceEnum, Boolean superPasswordFlag) {
        long nowTimeMilli = System.currentTimeMillis();
        Claims jwtClaims = Jwts.claims();
        jwtClaims.put(JwtConst.CLAIM_ID_KEY, userId);
        jwtClaims.put(JwtConst.CLAIM_NAME_KEY, userName);
        jwtClaims.put(JwtConst.CLAIM_USER_TYPE_KEY, userTypeEnum.getValue());
        jwtClaims.put(JwtConst.CLAIM_DEVICE_KEY, loginDeviceEnum.getValue());
        jwtClaims.put(JwtConst.CLAIM_SUPER_PASSWORD_FLAG, superPasswordFlag);
        JwtBuilder jwtBuilder = Jwts.builder()
                .setClaims(jwtClaims)
                .setIssuedAt(new Date(nowTimeMilli))
                .signWith(SignatureAlgorithm.HS512, tokenKey);

        // 如果是万能密码，则不需要记录到redis中;万能密码最多半个小时有效期
        if (superPasswordFlag) {
            jwtBuilder.setExpiration(new Date(nowTimeMilli + (HOUR_TIME_MILLI / 2)));
            return jwtBuilder.compact();
        }

        jwtBuilder.setExpiration(new Date(nowTimeMilli + tokenExpire * 24 * HOUR_TIME_MILLI));
        String token = jwtBuilder.compact();
        String redisKey = this.generateTokenRedisKey(userId, userTypeEnum.getValue(), loginDeviceEnum.getValue());
        redisService.set(redisKey, token, tokenExpire * 24 * 3600);
        return token;
    }

    /**
     * 生成登录信息： 含设备信息
     *
     * @param userId
     * @param device
     * @return
     */
    private String generateTokenRedisKey(Long userId, Integer userType, Integer device) {
        String userKey = userType + "_" + userId + "_" + device;
        return redisService.generateRedisKey(RedisKeyConst.Support.TOKEN, userKey);
    }


    /**
     * 强制移除 此用户各端的登录信息
     *
     * @param token
     */
    public void removeToken(String token) {
        Map<String, Object> tokenData = this.decryptTokenData(token);
        if (MapUtils.isEmpty(tokenData)) {
            return;
        }

        //特殊账号
        if (tokenData.get(JwtConst.CLAIM_SUPER_PASSWORD_FLAG) != null) {
            try {
                Boolean superPasswordFlag = Boolean.valueOf(tokenData.get(JwtConst.CLAIM_SUPER_PASSWORD_FLAG).toString());
                if (superPasswordFlag) {
                    return;
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return;
            }
        }

        boolean isValid = this.checkRedisToken(tokenData, token);
        if (!isValid) {
            return;
        }

        Long userId = Long.valueOf(tokenData.get(JwtConst.CLAIM_ID_KEY).toString());
        Integer userType = Integer.valueOf(tokenData.get(JwtConst.CLAIM_USER_TYPE_KEY).toString());
        Integer device = Integer.valueOf(tokenData.get(JwtConst.CLAIM_DEVICE_KEY).toString());

        String redisKey = this.generateTokenRedisKey(userId, userType, device);
        redisService.delete(redisKey);
    }

    /**
     * 解析并校验token信息 获取 userId
     *
     * @param token
     * @return
     */
    public Long getUserIdAndValidateToken(String token) {
        Map<String, Object> parseJwtData = this.decryptTokenData(token);
        boolean isValid = this.checkRedisToken(parseJwtData, token);
        if (!isValid) {
            return null;
        }
        Long userId = Long.valueOf(parseJwtData.get(JwtConst.CLAIM_ID_KEY).toString());
        return userId;
    }

    /**
     * 解密和解析token
     *
     * @param token
     * @return
     */
    private Map<String, Object> decryptTokenData(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(tokenKey)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 校验token是否有效
     *
     * @param token
     * @return
     */
    private boolean checkRedisToken(Map<String, Object> parseJwtData, String token) {
        if (MapUtils.isEmpty(parseJwtData)) {
            return false;
        }
        //特殊账号
        if (parseJwtData.get(JwtConst.CLAIM_SUPER_PASSWORD_FLAG) != null) {
            try {
                Boolean superPasswordFlag = Boolean.valueOf(parseJwtData.get(JwtConst.CLAIM_SUPER_PASSWORD_FLAG).toString());
                if (superPasswordFlag) {
                    return true;
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return false;
            }
        }

        Long userId = null;
        Integer userType = null, device = null;

        if (null != parseJwtData.get(JwtConst.CLAIM_ID_KEY)) {
            userId = NumberUtils.toLong(parseJwtData.get(JwtConst.CLAIM_ID_KEY).toString(), -1);
            userId = userId == -1 ? null : userId;
        }

        if (null != parseJwtData.get(JwtConst.CLAIM_USER_TYPE_KEY)) {
            userType = NumberUtils.toInt(parseJwtData.get(JwtConst.CLAIM_USER_TYPE_KEY).toString(), -1);
            userType = userType == -1 ? null : userType;
        }

        if (null != parseJwtData.get(JwtConst.CLAIM_DEVICE_KEY)) {
            device = NumberUtils.toInt(parseJwtData.get(JwtConst.CLAIM_DEVICE_KEY).toString(), -1);
            device = device == -1 ? null : device;
        }

        if (userId == null || userType == null || device == null) {
            return false;
        }

        String redisKey = this.generateTokenRedisKey(userId, userType, device);
        String redisToken = redisService.get(redisKey);
        return token.equals(redisToken);
    }

    /**
     * 批量移除用户所有设备的token
     */
    public void batchRemoveRedisToken(Long userId, UserTypeEnum userTypeEnum) {
        for (LoginDeviceEnum device : LoginDeviceEnum.values()) {
            redisService.delete(this.generateTokenRedisKey(userId, userTypeEnum.getValue(), device.getValue()));
        }
    }
}