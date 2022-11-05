package net.lab1024.sa.admin.module.business.goods.constant;


import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.sa.common.common.enumeration.BaseEnum;

/**
 * 商品状态
 *
 * @Author 1024创新实验室: 胡克
 * @Date 2021-10-25 20:26:54
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ），2012-2022
 */
@AllArgsConstructor
@Getter
public enum GoodsStatusEnum implements BaseEnum {

    /**
     * 1 预约中
     */
    APPOINTMENT(1, "预约中"),

    /**
     * 2 售卖
     */
    SELL(2, "售卖中"),

    /**
     * 3 售罄
     */
    SELL_OUT(3, "售罄"),


    ;

    private final Integer value;

    private final String desc;
}
