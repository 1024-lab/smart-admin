package net.lab1024.sa.base.module.support.repeatsubmit.ticket;

import jakarta.servlet.http.HttpServletRequest;

import java.util.function.Function;

/**
 * 凭证（用于校验重复提交的东西）
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2025-07-26 23:56:58
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
public abstract class AbstractRepeatSubmitTicket {

    private final Function<HttpServletRequest, String> generateTicketFunction;


    public AbstractRepeatSubmitTicket(Function<HttpServletRequest, String> generateTicketFunction) {
        this.generateTicketFunction = generateTicketFunction;
    }


    /**
     * 生成 加锁的 凭证
     */
    public String generateTicket(HttpServletRequest request) {
        return this.generateTicketFunction.apply(request);
    }

    /**
     * 加锁
     */
    public abstract boolean tryLock(String ticket, Long currentTimestamp, Long intervalMilliSecond);

    /**
     * 移除锁
     */
    public abstract void unLock(String ticket, Long intervalMilliSecond);

}
