package net.lab1024.smartadmin.util;

import net.lab1024.smartadmin.common.domain.BaseEnum;

/**
 * 枚举工具类
 *
 * @author listen
 * @date 2017/10/10 18:17
 */
public class SmartBaseEnumUtil {

    /**
     * 校验int类型的参数与枚举类比较是否合法
     *
     * @param value 参数
     * @param enumClass 枚举类必须实现BaseEnum接口
     * @return boolean
     * @Author listen
     */
    public static boolean checkEnum(Integer value, Class<? extends BaseEnum> enumClass) {
        if (null == value) {
            return false;
        }
        BaseEnum[] enums = enumClass.getEnumConstants();
        for (BaseEnum baseEnum : enums) {
            if (baseEnum.equalsValue(value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取枚举类的说明 value : info 的形式
     *
     * @param enumClass
     * @return String
     */
    public static String getEnumDesc(Class<? extends BaseEnum> enumClass) {
        BaseEnum[] enums = enumClass.getEnumConstants();
        // value : info 的形式
        StringBuilder sb = new StringBuilder();
        for (BaseEnum baseEnum : enums) {
            sb.append(baseEnum.getValue() + "：" + baseEnum.getDesc() + "，");
        }
        return sb.toString();
    }

    /**
     * 获取与int Code相匹配的枚举类的info
     *
     * @param value 参数
     * @param enumClass 枚举类必须实现BaseEnum接口
     * @return String 如无匹配枚举则返回null
     */
    public static String getEnumDescByValue(Integer value, Class<? extends BaseEnum> enumClass) {
        BaseEnum[] enums = enumClass.getEnumConstants();
        for (BaseEnum baseEnum : enums) {
            if (baseEnum.equalsValue(value)) {
                return baseEnum.getDesc();
            }
        }
        return null;
    }

    /**
     * 根据int类型的参数与获取枚举类的实例
     *
     * @param value 参数
     * @param enumClass 枚举类必须实现BaseEnum接口
     * @return BaseEnum 无匹配值返回null
     * @Author listen
     */
    public static <T extends BaseEnum> T getEnumByValue(Object value, Class<T> enumClass) {
        T[] enums = enumClass.getEnumConstants();
        for (T baseEnum : enums) {
            if (baseEnum.equalsValue(value)) {
                return baseEnum;
            }
        }
        return null;
    }

}
