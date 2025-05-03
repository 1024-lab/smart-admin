package net.lab1024.sa.base.module.support.cache;

import org.springframework.stereotype.Service;

import java.util.List;

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