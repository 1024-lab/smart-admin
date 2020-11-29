package net.lab1024.smartadmin.module.system.datascope.constant;


import net.lab1024.smartadmin.common.domain.BaseEnum;

import java.util.Arrays;
import java.util.Optional;

/**
 * [  ]
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2018 1024lab.netInc. All rights reserved.
 * @date 2019/4/28 0028 下午 15:37
 * @since JDK1.8
 */
public enum DataScopeViewTypeEnum implements BaseEnum {

    ME(0,0,"本人"),

    DEPARTMENT(1,5,"本部门"),

    DEPARTMENT_AND_SUB(2,10,"本部门及下属子部门"),

    ALL(3,15,"全部");

    private Integer value;
    private Integer level;
    private String desc;

    DataScopeViewTypeEnum(Integer value,Integer level, String desc) {
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
