package net.lab1024.sa.base.module.support.dict.constant;

import lombok.Getter;
import net.lab1024.sa.base.common.enumeration.BaseEnum;

/**
 * 字典回显样式 枚举
 */
@Getter
public enum DictDataStyleEnum implements BaseEnum {

    DEFAULT("默认", "default"),
    PRIMARY("主要", "primary"),
    SUCCESS("成功", "success"),
    INFO("信息", "info"),
    WARNING("警告", "warning"),
    DANGER("危险", "danger");

    private final String value;

    private final String desc;

    DictDataStyleEnum(String desc, String value) {
        this.desc = desc;
        this.value = value;
    }
}
