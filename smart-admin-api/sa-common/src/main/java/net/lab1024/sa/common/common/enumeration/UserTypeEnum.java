package net.lab1024.sa.common.common.enumeration;

/**
 * 用户类型
 *
 * @Author 1024创新实验室-主任:卓大
 * @Date 2022/10/19 21:46:24
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ），2012-2022
 */
public enum UserTypeEnum implements BaseEnum {

    /**
     * 管理端 员工用户
     */
    ADMIN_EMPLOYEE(1, "员工");

    private Integer type;

    private String desc;

    UserTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    @Override
    public Integer getValue() {
        return type;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}
