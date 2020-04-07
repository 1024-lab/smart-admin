package net.lab1024.smartadmin.module.support.quartz.constant;

/**
 * [  ]
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2018 1024lab.netInc. All rights reserved.
 * @date 2019/4/13 0013 下午 14:19
 * @since JDK1.8
 */
public enum TaskResultEnum {

    SUCCESS(0,"成功"),
    /**
     *
     */
    FAIL(1,"失败");

    public static final String INFO="0:成功，1:失败";

    private Integer status;

    private String desc;

    TaskResultEnum(Integer status , String desc) {
        this.status = status;
        this.desc = desc;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
