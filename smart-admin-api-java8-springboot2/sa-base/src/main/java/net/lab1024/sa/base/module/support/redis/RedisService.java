package net.lab1024.sa.base.module.support.redis;

import com.alibaba.fastjson.JSON;
import net.lab1024.sa.base.common.domain.SystemEnvironment;
import net.lab1024.sa.base.common.enumeration.SystemEnvironmentEnum;
import net.lab1024.sa.base.common.util.SmartStringUtil;
import net.lab1024.sa.base.constant.RedisKeyConst;
import org.slf4j.Logger;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * redis 一顿操作
 *
 * @Author 1024创新实验室: 罗伊
 * @Date 2020/8/25 21:57
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Service
public class RedisService {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(RedisService.class);

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private ValueOperations<String, String> redisValueOperations;

    @Resource
    private HashOperations<String, String, Object> redisHashOperations;

    @Resource
    private ListOperations<String, Object> redisListOperations;

    @Resource
    private SetOperations<String, Object> redisSetOperations;

    @Resource
    private SystemEnvironment systemEnvironment;


    /**
     * 生成redis key
     * @param prefix
     * @param key
     * @return
     */
    public String generateRedisKey(String prefix, String key) {
        SystemEnvironmentEnum currentEnvironment = systemEnvironment.getCurrentEnvironment();
        return systemEnvironment.getProjectName() + RedisKeyConst.SEPARATOR + currentEnvironment.getValue() +  RedisKeyConst.SEPARATOR + prefix + key;
    }

    /**
     * redis key 解析成真实的内容
     * @param redisKey
     * @return
     */
    public static String redisKeyParse(String redisKey) {
        if(SmartStringUtil.isBlank(redisKey)){
            return "";
        }
        int index = redisKey.lastIndexOf(RedisKeyConst.SEPARATOR);
        if(index < 1){
            return redisKey;
        }
        return redisKey.substring(index);
    }

    public boolean getLock(String key, long expire) {
        return redisValueOperations.setIfAbsent(key, String.valueOf(System.currentTimeMillis()), expire, TimeUnit.MILLISECONDS);
    }

    public void unLock(String key) {
        redisValueOperations.getOperations().delete(key);
    }

    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     * @return
     */
    public boolean expire(String key, long time) {
        return redisTemplate.expire(key, time, TimeUnit.SECONDS);
    }

    /**
     * 获取当天剩余的秒数
     *
     * @return
     */
    public static long currentDaySecond() {
        return ChronoUnit.SECONDS.between(LocalDateTime.now(), LocalDateTime.of(LocalDate.now(), LocalTime.MAX));
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
    public void delete(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete((Collection<String>) CollectionUtils.arrayToList(key));
            }
        }
    }

    /**
     * 删除缓存
     *
     * @param keyList
     */
    public void delete(List<String> keyList) {
        if (CollectionUtils.isEmpty(keyList)) {
            return;
        }
        redisTemplate.delete(keyList);
    }

    //============================String=============================

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public String get(String key) {
        return key == null ? null : redisValueOperations.get(key);
    }

    public <T> T getObject(String key, Class<T> clazz) {
        Object json = this.get(key);
        if (json == null) {
            return null;
        }
        T obj = JSON.parseObject(json.toString(), clazz);
        return obj;
    }


    /**
     * 普通缓存放入
     */
    public void set(String key, String value) {
        redisValueOperations.set(key, value);
    }
    public void set(Object key, Object value) {
        String jsonString = JSON.toJSONString(value);
        redisValueOperations.set(key.toString(), jsonString);
    }

    /**
     * 普通缓存放入
     */
    public void set(String key, String value, long second) {
        redisValueOperations.set(key, value, second, TimeUnit.SECONDS);
    }

    /**
     * 普通缓存放入并设置时间
     */
    public void set(Object key, Object value, long second) {
        String jsonString = JSON.toJSONString(value);
        if (second > 0) {
            redisValueOperations.set(key.toString(), jsonString, second, TimeUnit.SECONDS);
        } else {
            set(key.toString(), jsonString);
        }
    }

    //============================ map =============================


    public void mset(String key, String hashKey, Object value) {
        redisHashOperations.put(key, hashKey, value);
    }

    public Object mget(String key, String hashKey) {
        return redisHashOperations.get(key, hashKey);
    }

}