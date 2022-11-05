package net.lab1024.smartadmin.module.system.employee.constant;

import net.lab1024.smartadmin.common.domain.BaseEnum;

/**
 * [  ]
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2018 1024lab.netInc. All rights reserved.
 * @date 2019/3/27 0027 下午 16:22
 * @since JDK1.8
 */

public enum EmployeeStatusEnum implements BaseEnum {

    /**
     * 用户正常状态 1
     */
    NORMAL(0, "正常"),

    /**
     * 用户已被禁用 0
     */
    DISABLED(1, "禁用");

    private Integer value;

    private String desc;

    EmployeeStatusEnum(Integer value, String desc) {
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
        return this.desc;
    }
}