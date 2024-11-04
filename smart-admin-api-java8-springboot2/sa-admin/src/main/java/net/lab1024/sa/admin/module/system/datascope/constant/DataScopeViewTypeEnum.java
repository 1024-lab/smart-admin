package net.lab1024.sa.admin.module.system.datascope.constant;


import net.lab1024.sa.base.common.enumeration.BaseEnum;


/**
 * 数据范围 种类
 *
 * @Author 1024创新实验室: 罗伊
 * @Date 2020/11/28  20:59:17
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
public enum DataScopeViewTypeEnum implements BaseEnum {

    /**
     * 本人
     */
    ME(0, 0, "本人"),

    /**
     * 部门
     */
    DEPARTMENT(1, 5, "本部门"),

    /**
     * 本部门及下属子部门
     */
    DEPARTMENT_AND_SUB(2, 10, "本部门及下属子部门"),

    /**
     * 全部
     */
    ALL(10, 100, "全部");



    private final Integer value;
    private final Integer level;
    private final String desc;

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
