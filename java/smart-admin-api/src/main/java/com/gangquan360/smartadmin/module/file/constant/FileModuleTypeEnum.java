package com.gangquan360.smartadmin.module.file.constant;

import com.gangquan360.smartadmin.common.domain.BaseEnum;

/**
 * []
 *
 * @author yandanyang
 * @version 1.0
 * @since JDK1.8
 */
public enum FileModuleTypeEnum implements BaseEnum {

    /**
     * path 首字符不能包含\ 或者/
     */

    BACK_USER(1, "backUser/config", "backUser");

    private Integer value;

    private String path;

    private String desc;

    FileModuleTypeEnum(Integer value, String path, String desc) {
        this.value = value;
        this.path = path;
        this.desc = desc;
    }

    public String getPath() {
        return path;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    @Override
    public String getDesc() {
        return this.desc;
    }
}
