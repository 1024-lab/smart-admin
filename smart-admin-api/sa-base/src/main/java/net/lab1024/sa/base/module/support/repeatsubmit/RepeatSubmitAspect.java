package net.lab1024.sa.base.module.support.repeatsubmit;

import jakarta.servlet.http.HttpServletRequest;
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
 * 重复提交 aop切口 <br>
 * -------------------------<br>
 * 着重说明：<br>
 * 注解属性 intervalMilliSecond 是指 一段时间内只允许有一次请求；<br>
 * intervalMilliSecond = 0: 表示只有上个请求执行完以后才可以执行<br>
 * intervalMilliSecond > 0: 表示指定时间内只才能执行，特别提醒<br>
 * ------------------------<br>
 * 特殊说明 intervalMilliSecond > 0 <br>
 * 若假设 方法执行时间为 100ms，若 intervalMilliSecond = 50，则 同一时间内可能会有2个请求同时在执行！<br>
 * 若假设 方法执行时间为 100ms，若 intervalMilliSecond = 200，则 同一时间内只能有1请求执行，且执行完后100ms，才会执行下一次请求<br>
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2025-07-26 23:56:58
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Aspect
@Slf4j
public class RepeatSubmitAspect {

    private final AbstractRepeatSubmitTicket repeatSubmitTicket;

    /**
     * 获取凭证信息
     */
    public RepeatSubmitAspect(AbstractRepeatSubmitTicket repeatSubmitTicket) {
        this.repeatSubmitTicket = repeatSubmitTicket;
    }

    /**
     * 定义切入点
     */
    @Around("@annotation(net.lab1024.sa.base.module.support.repeatsubmit.annoation.RepeatSubmit)")
    public Object around(ProceedingJoinPoint point) throws Throwable {


        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return point.proceed();
        }

        /**
         * 第一步：生成防重复提交的 ticket凭证
         * ticket 是根据 Request对象 自定义 生成的，可以加入请求user相关属性作为生成要素
         */

        HttpServletRequest request = attributes.getRequest();
        String ticket = this.repeatSubmitTicket.generateTicket(request);
        if (StringUtils.isEmpty(ticket)) {
            return point.proceed();
        }

        Method method = ((MethodSignature) point.getSignature()).getMethod();
        RepeatSubmit annotation = method.getAnnotation(RepeatSubmit.class);
        Long intervalMilliSecond = (long) annotation.intervalMilliSecond();

        /**
         * 第二步：根据 ticket 凭证进行 加锁
         * 能加锁，则可以执行
         * 若不能加锁，则证明还是时间间隔interval中
         */

        boolean lockSuccessFlag = this.repeatSubmitTicket.tryLock(ticket, System.currentTimeMillis(), intervalMilliSecond);
        if (!lockSuccessFlag) {
            return ResponseDTO.error(UserErrorCode.REPEAT_SUBMIT);
        }

        try {
            return point.proceed();
        } catch (Throwable throwable) {
            log.error(throwable.getMessage(), throwable);
            throw throwable;
        } finally {
            this.repeatSubmitTicket.unLock(ticket, intervalMilliSecond);
        }
    }

}
