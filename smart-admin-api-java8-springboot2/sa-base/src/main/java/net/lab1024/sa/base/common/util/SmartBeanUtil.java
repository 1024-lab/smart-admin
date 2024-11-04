package net.lab1024.sa.base.common.util;

import org.springframework.beans.BeanUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * bean相关工具类
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2018-01-15 10:48:23
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
public class SmartBeanUtil {

    /**
     * 验证器
     */
    private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

    /**
     * 复制bean的属性
     *
     * @param source 源 要复制的对象
     * @param target 目标 复制到此对象
     */
    public static void copyProperties(Object source, Object target) {
        BeanUtils.copyProperties(source, target);
    }

    /**
     * 复制对象
     *
     * @param source 源 要复制的对象
     * @param target 目标 复制到此对象
     * @param <T>
     * @return
     */
    public static <T> T copy(Object source, Class<T> target) {
        if (source == null || target == null) {
            return null;
        }
        try {
            T newInstance = target.newInstance();
            BeanUtils.copyProperties(source, newInstance);
            return newInstance;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 复制list
     *
     * @param source
     * @param target
     * @param <T>
     * @param <K>
     * @return
     */
    public static <T, K> List<K> copyList(List<T> source, Class<K> target) {
        if (null == source || source.isEmpty()) {
            return Collections.emptyList();
        }
        return source.stream().map(e -> copy(e, target)).collect(Collectors.toList());
    }

    /**
     * 手动验证对象 Model的属性
     * 需要配合 hibernate-validator 校验注解
     *
     * @param t
     * @return String 返回null代表验证通过，否则返回错误的信息
     */
    public static <T> String verify(T t) {
        // 获取验证结果
        Set<ConstraintViolation<T>> validate = VALIDATOR.validate(t);
        if (validate.isEmpty()) {
            // 验证通过
            return null;
        }
        // 返回错误信息
        List<String> messageList = validate.stream().map(ConstraintViolation::getMessage).collect(Collectors.toList());
        return messageList.toString();
    }
}
