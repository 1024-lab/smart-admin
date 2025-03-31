package net.lab1024.sa.base.module.support.cache;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import jakarta.annotation.Resource;
import net.lab1024.sa.base.constant.ReloadConst;
import net.lab1024.sa.base.module.support.reload.core.annoation.SmartReload;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * redis 缓存实现
 *
 * @author zhoumingfa
 * @date 2025/3/28
 */
public class RedisCacheServiceImpl implements CacheService {

    @Resource
    private RedisCacheManager redisCacheManager;

    @Resource
    private RedisConnectionFactory redisConnectionFactory;

    /**
     * 获取所有缓存名称
     */
    @Override
    public List<String> cacheNames() {
        return Lists.newArrayList(redisCacheManager.getCacheNames());
    }

    /**
     * 某个缓存下的所有 key
     */
    @Override
    public List<String> cacheKey(String cacheName) {
        RedisCache cache = (RedisCache) redisCacheManager.getCache(cacheName);
        if (cache == null) {
            return Lists.newArrayList();
        }
        // 获取 Redis 连接
        RedisConnection connection = redisConnectionFactory.getConnection();
        // 根据指定的 key 模式获取所有匹配的键
        Set<byte[]> keys = connection.keyCommands().keys((cacheName + ":*").getBytes());

        if (keys != null) {
            return keys.stream().map(key -> {
                String redisKey = StrUtil.str(key, "utf-8");
                // 从 Redis 键中提取出最后一个冒号后面的字符串作为真正的键
                return redisKey.substring(redisKey.lastIndexOf(":") + 1);
            }).collect(Collectors.toList());
        }
        connection.close();
        return Lists.newArrayList(cacheName);
    }

    /**
     * 移除某个 key
     */
    @Override
    public void removeCache(String cacheName) {
        RedisCache cache = (RedisCache) redisCacheManager.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        }
    }

    @SmartReload(ReloadConst.CACHE_SERVICE)
    public void clearAllCache() {
        Collection<String> cacheNames = redisCacheManager.getCacheNames();
        for (String name : cacheNames) {
            RedisCache cache = (RedisCache) redisCacheManager.getCache(name);
            if (cache != null) {
                cache.clear();
            }
        }
    }
}
