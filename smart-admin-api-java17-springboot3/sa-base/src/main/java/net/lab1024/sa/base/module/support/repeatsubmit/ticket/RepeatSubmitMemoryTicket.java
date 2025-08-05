package net.lab1024.sa.base.module.support.repeatsubmit.ticket;

import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import com.google.common.collect.Maps;
import jakarta.servlet.http.HttpServletRequest;

import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

/**
 * 凭证（内存实现）
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2025-07-26 23:56:58
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
public class RepeatSubmitMemoryTicket extends AbstractRepeatSubmitTicket {

    private Interner<String> pool = Interners.newStrongInterner();

    private ConcurrentMap<String, Long> ticketMap = Maps.newConcurrentMap();

    public RepeatSubmitMemoryTicket(Function<HttpServletRequest, String> ticketFunction) {
        super(ticketFunction);
    }

    @Override
    public boolean tryLock(String ticket, Long currentTimestamp, Long intervalMilliSecond) {
        synchronized (pool.intern(ticket)) {
            Long lastTime = ticketMap.putIfAbsent(ticket, currentTimestamp);
            if (lastTime == null) {
                return true;
            }

            if (intervalMilliSecond <= 0) {
                return false;
            }

            if (currentTimestamp - lastTime < intervalMilliSecond) {
                return false;

            }
            ticketMap.put(ticket, currentTimestamp);
            return true;
        }
    }

    @Override
    public void unLock(String ticket, Long intervalMilliSecond) {
        if (intervalMilliSecond > 0) {
            return;
        }
        ticketMap.remove(ticket);
    }


}
