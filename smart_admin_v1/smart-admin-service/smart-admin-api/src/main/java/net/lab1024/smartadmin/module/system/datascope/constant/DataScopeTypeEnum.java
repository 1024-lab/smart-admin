package net.lab1024.smartadmin.module.system.datascope.constant;


import net.lab1024.smartadmin.common.domain.BaseEnum;

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
public enum DataScopeTypeEnum implements BaseEnum {

    DEFAULT(0,0,"默认类型","数据范围样例");

    private Integer value;
    private Integer sort;
    private String name;
    private String desc;

    DataScopeTypeEnum(Integer value,Integer sort,String name,String desc) {
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
