package net.lab1024.smartadmin.common.validator.bigdecimal;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义的属性校验注解
 *
 * @author listen
 * @date 2018年3月20日 13:53:33
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BigDecimalValidator.class)// 自定义验证的处理类
public @interface CheckBigDecimal {

    /**
     * 与这个数值校验
     *
     * @return
     */
    String value();

    /**
     * 比较符 请使用 ComparisonSymbolEnum 枚举类
     *
     * @return
     */
    ComparisonSymbolEnum symbolEnum();

    /**
     * 默认的错误提示信息
     *
     * @return String
     */
    String message() default "非法的数值";

    /**
     * 是否必须 : 默认 true
     *
     * @return boolean
     */
    boolean required() default true;

    //下面这两个属性必须添加 :不然会报错
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
