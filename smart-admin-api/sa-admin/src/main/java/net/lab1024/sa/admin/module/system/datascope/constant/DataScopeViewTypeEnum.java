package net.lab1024.sa.admin.module.system.datascope.constant;


import net.lab1024.sa.common.common.enumeration.BaseEnum;


/**
 * 数据范围 种类
 *
 * @Author 1024创新实验室: 罗伊
 * @Date 2020/11/28  20:59:17
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
public enum DataScopeViewTypeEnum implements BaseEnum {

    ME(0, 0, "本人"),

    DEPARTMENT(1, 5, "本部门"),

    DEPARTMENT_AND_SUB(2, 10, "本部门及下属子部门"),

    ALL(10, 100, "全部");



    private Integer value;
    private Integer level;
    private String desc;

    DataScopeViewTypeEnum(Integer value, Integer level, String desc) {
        this.value = value;
        this.level = level;
        this.desc = desc;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    public Integer getLevel() {
        return level;
    }

    @Override
    public String getDesc() {
        return desc;
    }


}
