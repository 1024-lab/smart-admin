package net.lab1024.smartadmin.module.system.datascope.constant;

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
public enum DataScopeWhereInTypeEnum {

    EMPLOYEE(0,"以员工IN"),

    DEPARTMENT(1,"以部门IN");

    private Integer type;
    private String desc;

    DataScopeWhereInTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public Integer getType() {
        return type;
    }


    public String getDesc() {
        return desc;
    }


}
