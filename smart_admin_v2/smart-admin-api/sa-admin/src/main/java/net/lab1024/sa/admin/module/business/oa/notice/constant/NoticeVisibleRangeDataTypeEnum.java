package net.lab1024.sa.admin.module.business.oa.notice.constant;


import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.sa.common.common.enumeration.BaseEnum;

/**
 * 公告、通知 可见范围类型
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-08-12 21:40:39
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ），2012-2022
 */
@Getter
@AllArgsConstructor
public enum NoticeVisibleRangeDataTypeEnum implements BaseEnum {

    EMPLOYEE(1, "员工"),

    DEPARTMENT(2, "部门"),

    ;

    private final Integer value;

    private final String desc;
}
