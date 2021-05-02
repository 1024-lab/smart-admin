package net.lab1024.smartadmin.module.system.datascope.constant;

import net.lab1024.smartadmin.common.domain.BaseEnum;

/**
 * [  ]
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2018 1024lab.netInc. All rights reserved.
 * @date 2019/5/8 0008 下午 16:00
 * @since JDK1.8
 */
public enum DataScopeWhereInTypeEnum implements BaseEnum {

    EMPLOYEE(0,"以员工IN"),

    DEPARTMENT(1,"以部门IN"),

    CUSTOM_STRATEGY(2,"自定义策略");

    private Integer value;
    private String desc;

    DataScopeWhereInTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String getDesc() {
        return desc;
    }


}
