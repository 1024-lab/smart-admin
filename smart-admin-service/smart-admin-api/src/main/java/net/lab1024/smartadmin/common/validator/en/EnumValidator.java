package net.lab1024.smartadmin.common.validator.en;

import net.lab1024.smartadmin.common.domain.BaseEnum;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

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
        for (Object obj : list) {
            boolean hasEnum = this.hasEnum(obj);
            if (!hasEnum) {
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
