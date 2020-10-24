package net.lab1024.smartadmin.common.validator.en;

import com.google.common.collect.Lists;
import net.lab1024.smartadmin.common.domain.BaseEnum;
import net.lab1024.smartadmin.module.support.file.constant.FileServiceTypeEnum;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 枚举类校验器
 *
 * @author listen
 * @date 2017/11/11 15:34
 */
public class EnumValidator implements ConstraintValidator<CheckEnum, Object> {

    /**
     * 枚举类的类对象
     */
    private Class<? extends BaseEnum> enumClass;

    /**
     * 是否必须
     */
    private boolean required;

    @Override
    public void initialize(CheckEnum constraintAnnotation) {
        // 获取注解传入的枚举类对象
        enumClass = constraintAnnotation.enumClazz();
        required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        // 判断是否必须
        if (null == value) {
            return !required;
        }

        if (value instanceof List) {
            // 如果为 List 集合数据
            return this.checkList((List<Object>) value);
        }

        // 校验是否为合法的枚举值
        return this.hasEnum(value);
    }

    /**
     * 校验集合类型
     *
     * @param list
     * @return
     */
    private boolean checkList(List<Object> list) {
        if (required && list.isEmpty()) {
            // 必须的情况下 list 不能为空
            return false;
        }
        // 校验是否重复
        long count = list.stream().distinct().count();
        if (count != list.size()) {
            return false;
        }
        List<Object> enumValList = Stream.of(enumClass.getEnumConstants()).map(BaseEnum::getValue).collect(Collectors.toList());
        for (Object obj : list) {
            if (!enumValList.contains(obj)) {
                return false;
            }
        }
        return true;
    }

    private boolean hasEnum(Object value) {
        // 校验是否为合法的枚举值
        BaseEnum[] enums = enumClass.getEnumConstants();
        for (BaseEnum baseEnum : enums) {
            if (baseEnum.getValue().equals(value)) {
                return true;
            }
        }
        return false;
    }
}
