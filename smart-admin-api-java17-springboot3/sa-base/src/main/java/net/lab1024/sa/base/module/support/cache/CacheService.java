package net.lab1024.sa.base.module.support.cache;

import com.google.common.collect.Lists;
import jakarta.annotation.Resource;
import net.lab1024.sa.base.constant.ReloadConst;
import net.lab1024.sa.base.module.support.reload.core.annoation.SmartReload;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 缓存服务
 *
 * @Author 1024创新实验室: 罗伊
 * @Date 2021/10/11 20:07
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Service
public interface CacheService {

    /**
     * 获取所有缓存名称
     */
    List<String> cacheNames();

    /**
     * 某个缓存下的所有 key
     */
    List<String> cacheKey(String cacheName);

    /**
     * 移除某个 key
     */
    void removeCache(String cacheName);

}