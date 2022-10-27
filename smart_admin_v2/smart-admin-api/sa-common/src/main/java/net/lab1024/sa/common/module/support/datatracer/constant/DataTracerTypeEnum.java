package net.lab1024.sa.common.module.support.datatracer.constant;


import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.sa.common.common.enumeration.BaseEnum;

/**
 * 数据业务类型
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-07-23 19:38:52-
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@AllArgsConstructor
@Getter
public enum DataTracerTypeEnum implements BaseEnum {

    GOODS(1, "商品"),

    OA_NOTICE(2, "OA-通知公告"),

    OA_ENTERPRISE(3, "OA-企业信息"),

    ;

    private final Integer value;

    private final String desc;
}
