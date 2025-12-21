package net.lab1024.sa.admin.module.business.ai.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 消息表 实体类
 *
 * @Author Mission
 * @Date 2025-12-21 16:10:16
 * @Copyright Mission
 */

@Data
@TableName("t_ai_messages")
public class AiMessagesEntity {

    /**
     * id
     */
    @TableId
    private String id;

    /**
     * 会话ID
     */
    private String conversatonId;

    /**
     * 查询
     */
    private String query;

    /**
     * 消息
     */
    private String message;

    /**
     * 入参
     */
    private String inputs;

    /**
     * 回答
     */
    private String answer;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建人姓名
     */
    private String createByName;

    /**
     * 创建时间
     */
    private LocalDateTime createAt;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新人姓名
     */
    private String updateByName;

    /**
     * 更新时间
     */
    private LocalDateTime updateAt;

}
