package net.lab1024.sa.base.module.support.repeatsubmit.ticket;

import net.lab1024.sa.base.module.support.repeatsubmit.annoation.RepeatSubmit;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * 凭证（redis实现）
 *
 * @Author 1024创新实验室: 罗伊
 * @Date 2020-11-25 20:56:58
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
public class RepeatSubmitRedisTicket extends AbstractRepeatSubmitTicket {

    private ValueOperations<String, String> redisValueOperations;

    public RepeatSubmitRedisTicket(ValueOperations<String, String> redisValueOperations,
                                   Function<String, String> ticketFunction) {
        super(ticketFunction);
        this.redisValueOperations = redisValueOperations;
    }

    @Override
    public Long getTicketTimestamp(String ticket) {
        Long timeStamp = System.currentTimeMillis();
        boolean setFlag = redisValueOperations.setIfAbsent(ticket, String.valueOf(timeStamp), RepeatSubmit.MAX_INTERVAL, TimeUnit.MILLISECONDS);
        if (!setFlag) {
            timeStamp = Long.valueOf(redisValueOperations.get(ticket));
        }
        return timeStamp;
    }

    @Override
    public void putTicket(String ticket) {
        redisValueOperations.getOperations().delete(ticket);
        this.getTicketTimestamp(ticket);
    }

    @Override
    public void removeTicket(String ticket) {
        redisValueOperations.getOperations().delete(ticket);
    }
}
