package net.lab1024.sa.admin.module.system.menu.constant;


import net.lab1024.sa.base.common.enumeration.BaseEnum;

/**
 * 权限类型
 *
 * @Author 1024创新实验室: 善逸
 * @Date 2022-03-06 22:04:37
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
public enum MenuPermsTypeEnum implements BaseEnum {
    /**
     * sa-token
     */
    SA_TOKEN(1, "Sa-Token模式"),

    ;

    private final Integer value;

    private final String desc;


    MenuPermsTypeEnum(Integer value, String desc) {
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
