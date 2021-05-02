package net.lab1024.smartadmin.handler;

import com.alibaba.fastjson.JSON;
import net.lab1024.smartadmin.common.anno.OperateLog;
import net.lab1024.smartadmin.common.constant.JudgeEnum;
import net.lab1024.smartadmin.module.business.log.LogService;
import net.lab1024.smartadmin.module.business.log.useroperatelog.domain.UserOperateLogEntity;
import net.lab1024.smartadmin.module.system.login.domain.RequestTokenBO;
import net.lab1024.smartadmin.util.SmartRequestTokenUtil;
import net.lab1024.smartadmin.util.SmartStringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
/**
 * [  操作日志记录处理,对所有OperateLog注解的Controller进行操作日志监控 ]
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2019 1024lab.netInc. All rights reserved.
 * @date
 * @since JDK1.8
 */
@Slf4j
@Aspect
@Component
public class SmartOperateLogAspect {

    @Autowired
    private LogService logService;

    @Pointcut("execution(* net.lab1024.smartadmin.module..*Controller.*(..)))")
    public void logPointCut() {
    }

    @AfterReturning(pointcut = "logPointCut()")
    public void doAfterReturning(JoinPoint joinPoint) {
        handleLog(joinPoint, null);
    }

    @AfterThrowing(value = "logPointCut()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Exception e) {
        handleLog(joinPoint, e);
    }

    protected void handleLog(final JoinPoint joinPoint, final Exception e) {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            OperateLog operateLog = this.getAnnotationLog(joinPoint);
            if (operateLog == null) {
                return;
            }
            RequestTokenBO requestToken = SmartRequestTokenUtil.getRequestUser();
            if (requestToken == null) {
                return;
            }
            // 设置方法名称
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            String operateMethod = className + "." + methodName;
            Object[] args = joinPoint.getArgs();
            StringBuilder sb = new StringBuilder();
            for (Object obj : args) {
                sb.append(obj.getClass().getSimpleName());
                sb.append("[");
                sb.append(JSON.toJSONString(obj));
                sb.append("]");
            }
            String params = sb.toString();
            String failReason = null;
            Integer result = JudgeEnum.YES.getValue();
            if (e != null) {
                result = JudgeEnum.NO.getValue();
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw, true);
                e.printStackTrace(pw);
                failReason = sw.toString();
                pw.flush();
                pw.close();
                sw.flush();
                sw.close();
            }
            UserOperateLogEntity operateLogEntity =
                UserOperateLogEntity.builder().userId(requestToken.getRequestUserId()).userName(requestToken.getEmployeeBO().getActualName()).url(request.getRequestURI()).method(operateMethod).param(params).failReason(failReason).result(result).build();
            ApiOperation apiOperation = this.getApiOperation(joinPoint);
            if (apiOperation != null) {
                operateLogEntity.setContent(apiOperation.value());
            }
            Api api = this.getApi(joinPoint);
            if (api != null) {
                String[] tags = api.tags();
                operateLogEntity.setModule(SmartStringUtil.join(tags, ","));
            }
            logService.addLog(operateLogEntity);
        } catch (Exception exp) {
            log.error("保存操作日志异常:{}", exp.getMessage());
            exp.printStackTrace();
        }
    }

    private OperateLog getAnnotationLog(JoinPoint joinPoint) throws Exception {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        OperateLog classAnnotation = AnnotationUtils.findAnnotation(method.getDeclaringClass(), OperateLog.class);

        if (method != null) {
            return classAnnotation;
        }
        return null;
    }

    /**
     * swagger API
     *
     * @param joinPoint
     * @return
     * @throws Exception
     */
    private Api getApi(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        Api classAnnotation = AnnotationUtils.findAnnotation(method.getDeclaringClass(), Api.class);

        if (method != null) {
            return classAnnotation;
        }
        return null;
    }

    /**
     * swagger ApiOperation
     *
     * @param joinPoint
     * @return
     * @throws Exception
     */
    private ApiOperation getApiOperation(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        if (method != null) {
            return method.getAnnotation(ApiOperation.class);
        }
        return null;
    }

}
