package net.lab1024.sa.common.module.support.jwe;

import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.common.common.domain.ResponseDTO;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.util.Base64Utils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.function.Function;

/**
 * 加密数据切口
 *
 * @Author 1024创新实验室: 胡克
 * @Date 2020/11/25 10:46
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Slf4j
@Aspect
@Order(100)
public class JweAspect {

    private static final String MD5_SALT_FORMAT = "sa_%s_salt";

    private Function<HttpServletRequest, JweUserKey> userFunction;

    public JweAspect(Function<HttpServletRequest, JweUserKey> userFunction) {
        this.userFunction = userFunction;
    }

    @Before("@annotation(net.lab1024.sa.common.module.support.jwe.JweDecrypt)")
    public void before(JoinPoint joinPoint) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        JweDecrypt annotation = method.getAnnotation(JweDecrypt.class);
        if (annotation == null) {
            return;
        }
        Object[] params = joinPoint.getArgs();
        if (params == null) {
            return;
        }
        if (params.length == 0) {
            return;
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        JweUserKey user = this.userFunction.apply(request);
        if (user == null) {
            return;
        }
        Boolean decryptParamFlag = params[0] instanceof DecryptData;
        if (!decryptParamFlag) {
            return;
        }
        DecryptData decryptData = (DecryptData) params[0];
        String data = decryptData.getData();
        log.info("解密前数据：{}", data);

        String key = SecureUtil.md5(String.format(MD5_SALT_FORMAT, user.getUserId()));
        log.info("解密KEY数据：{}", key);
        //初始化向量是16位长度
        String iv = key.substring(0, 16);
        //解密
        AES aes = new AES(Mode.CTS, Padding.PKCS5Padding, key.getBytes(), iv.getBytes());
        data = aes.decryptStr(data);
        log.info("解密后数据：{}", data);
        //base64解码
        data = new String(Base64Utils.decodeFromString(data));
        log.info("base64解码后数据：{}", data);
        decryptData.setData(data);
    }


    @AfterReturning(returning = "object", pointcut = "@annotation(net.lab1024.sa.common.module.support.jwe.JweEncrypt)")
    public void afterReturning(JoinPoint joinPoint, Object object) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        JweEncrypt annotation = method.getAnnotation(JweEncrypt.class);
        if (annotation == null) {
            return;
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        JweUserKey user = this.userFunction.apply(request);
        if (user == null) {
            return;
        }
        try {
            ResponseDTO responseDTO = (ResponseDTO) object;
            Object data = responseDTO.getData();
            if (data == null) {
                return;
            }
            String jsonData = JSON.toJSONString(data, SerializerFeature.DisableCircularReferenceDetect);
            log.info("JSON 原数据：{}", jsonData);
            //base64编码
            jsonData = Base64Utils.encodeToString(jsonData.getBytes("utf-8"));
            log.info("JSON Base64数据：{}", jsonData);
            //加密秘钥
            String key = SecureUtil.md5(String.format(MD5_SALT_FORMAT, user.getUserId()));
            log.info("JSON MD5 KEY数据：{}", key);
            //初始化向量是16位长度
            String iv = key.substring(0, 16);
            //AES 加密
            AES aes = new AES(Mode.CTS, Padding.PKCS5Padding, key.getBytes(), iv.getBytes());
            data = aes.encryptBase64(jsonData);
            log.info("JSON ASE 加密数据：{}", jsonData);
            responseDTO.setData(jsonData);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return;
        }
    }

}
