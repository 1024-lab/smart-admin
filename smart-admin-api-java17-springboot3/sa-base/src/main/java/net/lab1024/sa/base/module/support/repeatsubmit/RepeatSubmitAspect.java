package net.lab1024.sa.base.module.support.repeatsubmit;

import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.base.common.code.UserErrorCode;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.module.support.repeatsubmit.annoation.RepeatSubmit;
import net.lab1024.sa.base.module.support.repeatsubmit.ticket.AbstractRepeatSubmitTicket;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;

/**
 * 重复提交 aop切口
 *
 * @Author 1024创新实验室: 胡克
 * @Date 2020-11-25 20:56:58
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Aspect
@Slf4j
public class RepeatSubmitAspect {

    private AbstractRepeatSubmitTicket repeatSubmitTicket;

    /**
     * 获取凭证信息
     *
     * @param repeatSubmitTicket
     */
    public RepeatSubmitAspect(AbstractRepeatSubmitTicket repeatSubmitTicket) {
        this.repeatSubmitTicket = repeatSubmitTicket;
    }

    /**
     * 定义切入点
     *
     * @param point
     * @return
     * @throws Throwable
     */
    @Around("@annotation(net.lab1024.sa.base.module.support.repeatsubmit.annoation.RepeatSubmit)")
    public Object around(ProceedingJoinPoint point) throws Throwable {

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String ticketToken = attributes.getRequest().getServletPath();
        String ticket = this.repeatSubmitTicket.getTicket(ticketToken);
        if (StringUtils.isEmpty(ticket)) {
            return point.proceed();
        }
        Long lastRequestTime = this.repeatSubmitTicket.getTicketTimestamp(ticket);
        if (lastRequestTime != null) {
            Method method = ((MethodSignature) point.getSignature()).getMethod();
            RepeatSubmit annotation = method.getAnnotation(RepeatSubmit.class);
            int interval = Math.min(annotation.value(), RepeatSubmit.MAX_INTERVAL);
            if (System.currentTimeMillis() < lastRequestTime + interval) {
                // 提交频繁
                return ResponseDTO.error(UserErrorCode.REPEAT_SUBMIT);
            }
        }
        Object obj = null;
        try {
            // 先给 ticket 设置在执行中
            this.repeatSubmitTicket.putTicket(ticket);
            obj = point.proceed();
        } catch (Throwable throwable) {
            log.error("", throwable);
            throw throwable;
        }
        return obj;
    }

}
