package net.lab1024.smartadmin.module.support.websocket.domain;

import net.lab1024.smartadmin.module.support.websocket.MessageTypeEnum;
import lombok.Builder;
import lombok.Data;

/**
 * [  ]
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2018 1024lab.netInc. All rights reserved.
 * @date 2019/7/10 0010 下午 18:50
 * @since JDK1.8
 */
@Data
@Builder
public class MessageDTO {

    /**
     * 消息类型 {@link MessageTypeEnum}
     */
    private Integer messageType;

    /**
     * 消息体
     */
    private String message;

    /**
     * 发送者
     */
    private Long fromUserId;

    /**
     * 接收者，系统通知可为null
     */
    private Long toUserId;

}
