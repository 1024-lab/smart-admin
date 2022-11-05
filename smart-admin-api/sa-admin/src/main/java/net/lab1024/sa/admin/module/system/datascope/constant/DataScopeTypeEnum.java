package net.lab1024.sa.admin.module.system.datascope.constant;

import net.lab1024.sa.common.common.enumeration.BaseEnum;

/**
 * 数据范围 类型
 *
 * @Author 1024创新实验室: 罗伊
 * @Date 2020/11/28  20:59:17
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
public enum DataScopeTypeEnum implements BaseEnum {

    /**
     * 系统通知
     */
    NOTICE(1, 20, "系统通知", "系统通知数据范围"),
    ;

    private Integer value;

    private Integer sort;

    private String name;

    private String desc;

    DataScopeTypeEnum(Integer value, Integer sort, String name, String desc) {
        this.value = value;
        this.sort = sort;
        this.name = name;
        this.desc = desc;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    public Integer getSort() {
        return sort;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    public String getName() {
        return name;
    }


}
