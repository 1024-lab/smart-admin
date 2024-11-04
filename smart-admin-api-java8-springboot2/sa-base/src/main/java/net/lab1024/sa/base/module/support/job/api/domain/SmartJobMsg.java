package net.lab1024.sa.base.module.support.job.api.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import net.lab1024.sa.base.common.enumeration.BaseEnum;

/**
 * 定时任务 发布/订阅消息对象
 *
 * @author huke
 * @date 2024/6/20 21:10
 */
@Data
public class SmartJobMsg {

    /**
     * 消息id 无需设置
     */
    private String msgId;

    /**
     * 任务id
     */
    private Integer jobId;

    /**
     * 任务参数
     */
    private String param;

    /**
     * 消息类型
     */
    private MsgTypeEnum msgType;

    /**
     * 更新人
     */
    private String updateName;

    @Getter
    @AllArgsConstructor
    public enum MsgTypeEnum implements BaseEnum {

        /**
         * 1 更新任务
         */
        UPDATE_JOB(1, "更新任务"),

        EXECUTE_JOB(2, "执行任务"),

        ;

        private final Integer value;

        private final String desc;
    }
}
