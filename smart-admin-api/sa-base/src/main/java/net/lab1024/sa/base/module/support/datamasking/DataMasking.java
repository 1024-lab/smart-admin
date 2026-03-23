package net.lab1024.sa.base.module.support.datamasking;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import net.lab1024.sa.base.common.json.serializer.DataMaskingSerializer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 脱敏注解
 *
 * @author 罗伊
 * @description:
 * @date 2024/7/21 4:39 下午
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonSerialize(using = DataMaskingSerializer.class, nullsUsing = DataMaskingSerializer.class)
public @interface DataMasking {

    DataMaskingTypeEnum value() default DataMaskingTypeEnum.COMMON;

}
