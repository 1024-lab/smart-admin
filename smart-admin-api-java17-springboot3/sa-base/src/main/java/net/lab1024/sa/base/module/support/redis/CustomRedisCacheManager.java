package net.lab1024.sa.base.module.support.redis;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.convert.DurationStyle;
import org.springframework.data.redis.cache.*;

import java.time.Duration;

import static net.lab1024.sa.base.common.constant.StringConst.COLON;

/**
 * 自定义 RedisCacheManager，支持在 cacheName 中通过 '#' 指定 TTL（过期时间）。
 *
 * @Author CoderKK
 * @Date 2025-08-15 13:01:01
 * <p>
 * 支持格式：{@code cacheName#ttl}，其中 ttl 支持 Spring 的 Duration 格式。
 * 特殊值：{@code -1} 表示永久缓存（永不过期）。
 * </p>
 *
 * <h3>使用示例：</h3>
 * <pre>
 * // 10 秒后过期
 * &#64;Cacheable(value = "user#10s", key = "#id")
 * // 2 小时后过期
 * &#64;Cacheable(value = "report#2h", key = "#date")
 * // 30 分钟后过期
 * &#64;Cacheable(value = "session#30m", key = "#token")
 * // 永不过期（永久缓存），适用于极少变化的配置数据
 * &#64;Cacheable(value = "appConfig#-1", key = "'globalSettings'")
 * // 无 TTL，使用全局默认过期时间（如 7 天）
 * &#64;Cacheable(value = "product", key = "#productId")
 * </pre>
 *
 * <h3>生成的 Redis Key 格式：</h3>
 * <pre>
 * cache:cacheName:key
 * 例如：cache:user:123
 *      cache:appConfig:globalSettings
 * </pre>
 *
 * <h3>支持的 TTL 单位：</h3>
 * <ul>
 *   <li>{@code ms} / {@code millis} / {@code milliseconds} - 毫秒</li>
 *   <li>{@code s} / {@code secs} / {@code seconds} - 秒</li>
 *   <li>{@code m} / {@code mins} / {@code minutes} - 分钟</li>
 *   <li>{@code h} / {@code hrs} / {@code hours} - 小时</li>
 *   <li>{@code d} / {@code days} - 天</li>
 * </ul>
 *
 * <h3>注意事项：</h3>
 * <ul>
 *   <li>不写单位默认为毫秒</li>
 *   <li>永久缓存（#-1）不会自动过期，请配合 &#64;CacheEvict 手动清理。</li>
 *   <li>避免对频繁更新的数据使用永久缓存，防止数据陈旧。</li>
 *   <li>cacheName 中的 '#' 只解析第一个，后续字符将作为 TTL 处理。</li>
 * </ul>
 */
@Slf4j
public class CustomRedisCacheManager extends RedisCacheManager {

    /**
     * 缓存全局前缀
     */
    private static final String CACHE_PREFIX = "cache";

    /**
     * 自定义 TTL 分隔符，用于在 cacheName 后附加过期时间
     */
    private static final String CUSTOM_TTL_SEPARATOR = "#";

    /**
     * 默认缓存过期时间：7 天
     */
    private static final Duration DEFAULT_TTL = Duration.ofDays(7);

    public CustomRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration) {
        super(cacheWriter, defaultCacheConfiguration);
    }

    /**
     * 创建 RedisCache 实例，支持从 cacheName 解析 TTL
     *
     * @param name        缓存名称（支持 name#ttl 格式）
     * @param cacheConfig 默认缓存配置
     * @return RedisCache
     */
    @Override
    protected RedisCache createRedisCache(String name, RedisCacheConfiguration cacheConfig) {
        Duration ttl = parseTtlFromCacheName(name);
        if (ttl == null) {
            ttl = DEFAULT_TTL;
        }

        CacheKeyPrefix keyPrefix = cacheName -> {
            if (StrUtil.isBlank(cacheName)) {
                return CACHE_PREFIX + COLON;
            }
            String[] parts = cacheName.split(CUSTOM_TTL_SEPARATOR, 2);
            String cleanName = StrUtil.trim(parts[0]);
            return CACHE_PREFIX + COLON + cleanName + COLON;
        };

        // 构建最终缓存配置：设置 key 前缀 + TTL
        RedisCacheConfiguration config = cacheConfig.computePrefixWith(keyPrefix).entryTtl(ttl);

        return super.createRedisCache(name, config);
    }

    /**
     * 从 cacheName 中解析 TTL
     *
     * @param name 缓存名称，格式如：users#10m, products#2h, config#-1（永久）
     * @return 解析出的 Duration若无效则返回 null；若为 -1，则返回 Duration.ofMillis(-1) 表示永久缓存
     */
    private Duration parseTtlFromCacheName(String name) {
        if (StrUtil.isBlank(name)) {
            return null;
        }

        String[] parts = name.split(CUSTOM_TTL_SEPARATOR, 2);
        if (parts.length < 2) {
            return null; // 无 TTL 部分
        }

        String ttlStr = StrUtil.trim(parts[1]);
        if (StrUtil.isBlank(ttlStr)) {
            return null;
        }

        // 特殊处理：-1 表示永久缓存
        if ("-1".equals(ttlStr)) {
            return Duration.ofMillis(-1); // Spring Redis 中负数 Duration 表示永不过期
        }

        try {
            Duration ttl = DurationStyle.detectAndParse(ttlStr);
            return ttl.getSeconds() > 0 ? ttl : null;
        } catch (IllegalArgumentException e) {
            log.error("解析缓存 TTL 失败，cacheName='{}', ttl='{}', 错误: {}", name, ttlStr, e);
            return null;
        }
    }
}
