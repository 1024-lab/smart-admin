package net.lab1024.sa.admin.module.business.ai.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 应用提供商 实体类
 *
 * @Author Mission
 * @Date 2025-12-21 16:06:19
 * @Copyright Mission
 */

@Data
@TableName("t_ai_app_provider")
public class AiAppProviderEntity {

    /**
     * id
     */
    @TableId
    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * baseUrl
     */
    private String baseUrl;

    /**
     * key
     */
    private String key;

    /**
     * 创建时间
     */
    private LocalDateTime createAt;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建人名称
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
     * 创建人名称
     */
    private String updateByName;

}
