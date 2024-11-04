package net.lab1024.sa.base.module.support.repeatsubmit.ticket;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import net.lab1024.sa.base.module.support.repeatsubmit.annoation.RepeatSubmit;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * 凭证（内存实现）
 *
 * @Author 1024创新实验室: 罗伊
 * @Date 2020-11-25 20:56:58
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
public class RepeatSubmitCaffeineTicket extends AbstractRepeatSubmitTicket {

    /**
     * 限制缓存最大数量 超过后先放入的会自动移除
     * 默认缓存时间
     * 初始大小为：100万
     */
    private static Cache<String, Long> cache = Caffeine.newBuilder()
            .maximumSize(100 * 10000)
            .expireAfterWrite(RepeatSubmit.MAX_INTERVAL, TimeUnit.MILLISECONDS).build();


    public RepeatSubmitCaffeineTicket(Function<String, String> ticketFunction) {
        super(ticketFunction);
    }

    @Override
    public Long getTicketTimestamp(String ticket) {
        return cache.getIfPresent(ticket);
    }


    @Override
    public void putTicket(String ticket) {
        cache.put(ticket, System.currentTimeMillis());
    }

    @Override
    public void removeTicket(String ticket) {
        cache.invalidate(ticket);
    }
}
