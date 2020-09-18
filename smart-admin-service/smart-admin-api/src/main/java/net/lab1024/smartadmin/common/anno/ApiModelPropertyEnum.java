package net.lab1024.smartadmin.common.anno;

import net.lab1024.smartadmin.common.domain.BaseEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 枚举类字段属性的注解
 *
 * @author listen
 * @date 2019/05/16 15:18
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiModelPropertyEnum {

    /**
     * 枚举类对象
     *
     * @return
     */
    Class<? extends BaseEnum> value();

    String example() default "";

    /**
     * 是否隐藏
     *
     * @return
     */
    boolean hidden() default false;

    /**
     * 是否必须
     *
     * @return
     */
    boolean required() default true;

    String dataType() default "";

    String enumDesc() default "";

}
