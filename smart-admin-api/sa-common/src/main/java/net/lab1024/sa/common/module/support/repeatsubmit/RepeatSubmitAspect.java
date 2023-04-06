package net.lab1024.sa.common.module.support.repeatsubmit;

import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.common.common.code.UserErrorCode;
import net.lab1024.sa.common.common.domain.ResponseDTO;
import net.lab1024.sa.common.module.support.repeatsubmit.annoation.RepeatSubmit;
import net.lab1024.sa.common.module.support.repeatsubmit.ticket.AbstractRepeatSubmitTicket;
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
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Aspect
@Slf4j
public class RepeatSubmitAspect {

    private AbstractRepeatSubmitTicket repeatSubmitTicket;

    /**
     * 获取凭证信息
     * rep
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
    @Around("@annotation(net.lab1024.sa.common.module.support.repeatsubmit.annoation.RepeatSubmit)")
    public Object around(ProceedingJoinPoint point) throws Throwable {

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String ticketToken = attributes.getRequest().getServletPath();
        String ticket = this.repeatSubmitTicket.getTicket(ticketToken);
        if (StringUtils.isEmpty(ticket)) {
            return point.proceed();
        }
        Long timeStamp = this.repeatSubmitTicket.getTicketTimestamp(ticket);
        if (timeStamp != null) {
            Method method = ((MethodSignature) point.getSignature()).getMethod();
            RepeatSubmit annotation = method.getAnnotation(RepeatSubmit.class);

            // 说明注解去掉了
            if (annotation != null) {
                return point.proceed();
            }

            int interval = Math.min(annotation.value(), RepeatSubmit.MAX_INTERVAL);
            if (System.currentTimeMillis() < timeStamp + interval) {
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
        } finally {
            this.repeatSubmitTicket.removeTicket(ticket);
        }
        return obj;
    }

}
