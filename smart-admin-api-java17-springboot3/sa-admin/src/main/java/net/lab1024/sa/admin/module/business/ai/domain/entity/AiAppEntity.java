package net.lab1024.sa.admin.module.business.ai.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * ai应用管理表 实体类
 *
 * @Author Mission
 * @Date 2025-12-21 16:00:44
 * @Copyright Mission
 */

@Data
@TableName("t_ai_app")
public class AiAppEntity {

    /**
     * id
     */
    @TableId
    private String id;

    /**
     * 应用名称
     */
    private String name;

    /**
     * 应用描述
     */
    private String description;

    /**
     * 应用提供商id
     */
    private String appProviderId;

    /**
     * 应用级别配置(JSON格式)
     */
    private String appConfigJson;

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
     * 更新时间
     */
    private LocalDateTime updateAt;

    /**
     * 更新人姓名
     */
    private String updateByName;

}
