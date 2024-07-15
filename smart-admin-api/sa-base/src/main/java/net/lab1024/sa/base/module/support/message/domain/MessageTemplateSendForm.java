package net.lab1024.sa.base.module.support.message.domain;

import lombok.Data;
import net.lab1024.sa.base.common.enumeration.UserTypeEnum;
import net.lab1024.sa.base.module.support.message.constant.MessageTemplateEnum;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * 消息发送form
 *
 * @author luoyi
 * @date 2024/06/22 20:20
 */
@Data
public class MessageTemplateSendForm {

    @NotNull(message = "消息子类型不能为空")
    private MessageTemplateEnum messageTemplateEnum;

    @NotNull(message = "接收者类型不能为空")
    private UserTypeEnum receiverUserType;

    @NotNull(message = "接收者id不能为空")
    private Long receiverUserId;

    /**
     * 相关业务id | 可选
     * 用于跳转具体业务
     */
    private Object dataId;

    /**
     * 消息参数 | 可选
     * 例：订单号：【{orderId}】{time}所提交的对账单被作废，请核实信息重新提交~
     * {orderId} {time} 就是消息的参数变量
     * 发送消息时 需要在map中放入k->orderId k->time
     */
    private Map<String, Object> contentParam;
}
