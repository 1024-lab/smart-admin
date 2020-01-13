package com.gangquan360.smartadmin.module.codegenerator.constant;


/**
 * [ gt,lt 目前只支持Date]
 *
 * @author yandanyang
 * @version 1.0
 * @since JDK1.8
 */
public enum SqlOperateTypeEnum{


    LIKE(1, "like"),
    EQUALS(2, "equals"),
    IN(3, "in"),
    TIME_EQUALS(5, "time_equals"),
    TIME_BETWEEN(6, "time_between");

    private Integer type;

    private String name;

    SqlOperateTypeEnum(Integer type, String name) {
        this.type = type;
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
