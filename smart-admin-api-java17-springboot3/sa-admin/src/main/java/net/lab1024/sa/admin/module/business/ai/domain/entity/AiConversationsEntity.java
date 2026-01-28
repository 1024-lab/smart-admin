package net.lab1024.sa.admin.module.business.ai.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 会话表 实体类
 *
 * @Author Mission
 * @Date 2025-12-21 16:08:28
 * @Copyright Mission
 */

@Data
@TableName("t_ai_conversations")
public class AiConversationsEntity {

    /**
     * id
     */
    @TableId
    private String id;

    /**
     * 应用id
     */
    private String appId;

    /**
     * 名称
     */
    private String name;

    /**
     * 入参
     */
    private String inputs;

    /**
     * 总结
     */
    private String summary;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建人姓名
     */
    private String createByName;

    /**
     * 更新时间
     */
    private LocalDateTime updateAt;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新人姓名
     */
    private String updateByName;

}
