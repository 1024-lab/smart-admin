package net.lab1024.sa.base.module.support.apiencrypt.advice;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.enumeration.DataTypeEnum;
import net.lab1024.sa.base.module.support.apiencrypt.annotation.ApiEncrypt;
import net.lab1024.sa.base.module.support.apiencrypt.service.ApiEncryptService;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.annotation.Resource;

/**
 * 加密
 *
 * @Author 1024创新实验室-主任:卓大
 * @Date 2023/10/24 09:52:58
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>，Since 2012
 */


@Slf4j
@ControllerAdvice
public class EncryptResponseAdvice implements ResponseBodyAdvice<ResponseDTO> {

    @Resource
    private ApiEncryptService apiEncryptService;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return returnType.hasMethodAnnotation(ApiEncrypt.class) || returnType.getContainingClass().isAnnotationPresent(ApiEncrypt.class);
    }

    @Override
    public ResponseDTO beforeBodyWrite(ResponseDTO body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body.getData() == null) {
            return body;
        }

        String encrypt = apiEncryptService.encrypt(JSON.toJSONString(body.getData()));
        body.setData(encrypt);
        body.setDataType(DataTypeEnum.ENCRYPT.getValue());
        return body;
    }
}


