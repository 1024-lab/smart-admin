package net.lab1024.sa.common.common.security;

import net.lab1024.sa.common.common.annoation.SaAuth;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.prepost.PreInvocationAttribute;
import org.springframework.security.access.prepost.PrePostAnnotationSecurityMetadataSource;
import org.springframework.security.access.prepost.PrePostInvocationAttributeFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

/**
 * 此类用于默认给所有接口添加权限 @saAuth.checkPermission('%s')
 * %s 为类名.方法名
 * 和使用@PreAuthorize("@saAuth.checkPermission('%s')") 效果一致
 * 避免所有接口都添加一遍 减轻工作量
 *
 * @Author 1024创新实验室: 罗伊
 * @Date 2021-08-30 23:08
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
public class SecurityMethodSource extends PrePostAnnotationSecurityMetadataSource {


    private static String EXPRESSION_FORMAT = "@%s.checkPermission('%s')";

    private final PrePostInvocationAttributeFactory attributeFactory;

    private String beanName;


    public SecurityMethodSource(PrePostInvocationAttributeFactory attributeFactory, String beanName) {
        super(attributeFactory);
        this.attributeFactory = attributeFactory;
        this.beanName = beanName;
    }


    @Override
    public Collection<ConfigAttribute> getAttributes(Method method, Class<?> targetClass) {
        //如果不存在SaAuth采用security认证模式
        SaAuth saAuth = method.getAnnotation(SaAuth.class);
        if (saAuth == null) {
            return super.getAttributes(method, targetClass);
        }

        //存在添加以URL为权限字符串的校验模式
        ArrayList<ConfigAttribute> configAttributes = new ArrayList(1);
        String classFullName = targetClass.getName();
        String methodName = method.getName();
        String[] classNameArray = StringUtils.split(classFullName, "\\.");
        String controllerName = classNameArray[classNameArray.length - 1];
        String privilegeName = controllerName + "." + methodName;
        String preAuthorizeAttribute = String.format(EXPRESSION_FORMAT, beanName, privilegeName);
        PreInvocationAttribute pre = this.attributeFactory.createPreInvocationAttribute(null, null, preAuthorizeAttribute);
        if (pre != null) {
            configAttributes.add(pre);
        }
        return configAttributes;
    }

}
