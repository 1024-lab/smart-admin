package net.lab1024.sa.base.module.support.apiencrypt.advice;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.base.common.util.SmartStringUtil;
import net.lab1024.sa.base.module.support.apiencrypt.annotation.ApiDecrypt;
import net.lab1024.sa.base.module.support.apiencrypt.domain.ApiEncryptForm;
import net.lab1024.sa.base.module.support.apiencrypt.service.ApiEncryptService;
import org.apache.commons.io.IOUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import javax.annotation.Resource;
import java.io.InputStream;
import java.lang.reflect.Type;

/**
 * 解密
 *
 * @Author 1024创新实验室-主任:卓大
 * @Date 2023/10/21 11:41:46
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>，Since 2012
 */

@Slf4j
@ControllerAdvice
public class DecryptRequestAdvice extends RequestBodyAdviceAdapter {

    private static final String ENCODING = "UTF-8";

    @Resource
    private ApiEncryptService apiEncryptService;

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return methodParameter.hasMethodAnnotation(ApiDecrypt.class) || methodParameter.hasParameterAnnotation(ApiDecrypt.class) || methodParameter.getContainingClass().isAnnotationPresent(ApiDecrypt.class);
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType)  {
        try {
            String bodyStr = IOUtils.toString(inputMessage.getBody(), ENCODING);
            ApiEncryptForm apiEncryptForm = JSONObject.parseObject(bodyStr, ApiEncryptForm.class);
            if (SmartStringUtil.isEmpty(apiEncryptForm.getEncryptData())) {
                return inputMessage;
            }
            String decrypt = apiEncryptService.decrypt(apiEncryptForm.getEncryptData());
            return new DecryptHttpInputMessage(inputMessage.getHeaders(), IOUtils.toInputStream(decrypt, ENCODING));
        } catch (Exception e) {
            log.error("", e);
            return inputMessage;
        }
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    static class DecryptHttpInputMessage implements HttpInputMessage {
        private final HttpHeaders headers;

        private final InputStream body;

        public DecryptHttpInputMessage(HttpHeaders headers, InputStream body) {
            this.headers = headers;
            this.body = body;
        }

        @Override
        public InputStream getBody() {
            return body;
        }

        @Override
        public HttpHeaders getHeaders() {
            return headers;
        }
    }

}
