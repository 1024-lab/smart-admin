package net.lab1024.sa.base.common.validator.enumeration;


import net.lab1024.sa.base.common.enumeration.BaseEnum;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 枚举类校验器
 *
 * @Author 1024创新实验室: 胡克
 * @Date 2017/11/11 15:34
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
public class EnumValidator implements ConstraintValidator<CheckEnum, Object> {

    /**
     * 枚举类实例集合
     */
    private List<Object> enumValList;

    /**
     * 是否必须
     */
    private boolean required;

    @Override
    public void initialize(CheckEnum constraintAnnotation) {
        // 获取注解传入的枚举类对象
        required = constraintAnnotation.required();
        Class<? extends BaseEnum> enumClass = constraintAnnotation.value();
        enumValList = Stream.of(enumClass.getEnumConstants()).map(BaseEnum::getValue).collect(Collectors.toList());
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
        return enumValList.contains(value);
    }

    /**
     * 校验集合类型
     *
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
        return enumValList.containsAll(list);
    }
}
