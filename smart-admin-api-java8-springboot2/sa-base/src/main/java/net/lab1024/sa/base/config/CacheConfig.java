package net.lab1024.sa.base.config;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import net.lab1024.sa.base.module.support.cache.CacheService;
import net.lab1024.sa.base.module.support.cache.CaffeineCacheServiceImpl;
import net.lab1024.sa.base.module.support.cache.RedisCacheServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import javax.annotation.Resource;

/**
 * 缓存配置
 *
 * @author zhoumingfa
 * @date 2025/03/28
 */
@Configuration
public class CacheConfig {

    private static final String REDIS_CACHE = "redis";
    private static final String CAFFEINE_CACHE = "caffeine";


    @Resource
    private RedisConnectionFactory factory;

    @Bean
    @ConditionalOnProperty(prefix = "spring.cache", name = {"type"}, havingValue = REDIS_CACHE)
    public RedisCacheConfiguration redisCacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues()
                .computePrefixWith(name -> "cache:" + name + ":")
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericFastJsonRedisSerializer()));
    }

    @Bean
    @ConditionalOnProperty(prefix = "spring.cache", name = {"type"}, havingValue = REDIS_CACHE)
    public CacheService redisCacheService() {
        return new RedisCacheServiceImpl();
    }

    @Bean
    @ConditionalOnProperty(prefix = "spring.cache", name = {"type"}, havingValue = CAFFEINE_CACHE)
    public CacheService caffeineCacheService() {
        return new CaffeineCacheServiceImpl();
    }

}