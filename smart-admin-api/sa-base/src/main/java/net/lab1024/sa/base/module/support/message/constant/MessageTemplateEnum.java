package net.lab1024.sa.base.module.support.message.constant;


import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.sa.base.common.enumeration.BaseEnum;

/**
 * 消息模板类型
 *
 * @author luoyi
 * @date 2024/06/22 20:20
 */
@Getter
@AllArgsConstructor
public enum MessageTemplateEnum implements BaseEnum {



    ORDER_AUDIT(1000, "订单审批", MessageTypeEnum.ORDER, "您有一个订单等待审批，订单号【${orderNumber}】"),

    ;

    private final Integer value;

    private final String desc;

    private final MessageTypeEnum messageTypeEnum;

    private final String content;
}
