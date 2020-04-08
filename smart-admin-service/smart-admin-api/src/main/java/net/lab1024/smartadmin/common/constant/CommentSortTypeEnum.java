package net.lab1024.smartadmin.common.constant;


import net.lab1024.smartadmin.common.domain.BaseEnum;

/**
 * 全局排序枚举类
 *
 * @author listen
 * @date 2018/01/13 14:24
 */
public enum CommentSortTypeEnum implements BaseEnum {

    /**
     * 正序 ASC 1
     */
    ASC(1, "ASC"),

    /**
     * 倒序 DESC 2
     */
    DESC(2, "DESC");

    private Integer value;

    private String desc;

    /**
     * 排序类型：1正序 | 2倒序
     */
    public static final String INFO = "排序类型：1正序 | 2倒序";

    CommentSortTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    /**
     * 获取枚举类的值
     *
     * @return Integer
     */
    @Override
    public Integer getValue() {
        return value;
    }

    /**
     * 获取枚举类的说明
     *
     * @return String
     */
    @Override
    public String getDesc() {
        return desc;
    }
}
