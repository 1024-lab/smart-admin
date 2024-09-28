package net.lab1024.sa.base.module.support.message.constant;


import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.sa.base.common.enumeration.BaseEnum;


/**
 * 消息类型
 *
 * @author luoyi
 * @date 2024/06/22 20:20
 */
@Getter
@AllArgsConstructor
public enum MessageTypeEnum implements BaseEnum {

    MAIL(1, "站内信"),

    ORDER(2, "订单"),
    ;

    private final Integer value;

    private final String desc;
}
