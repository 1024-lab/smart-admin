package net.lab1024.sa.admin.module.business.ai.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 模型配置表 实体类
 *
 * @Author Mission
 * @Date 2026-01-28 09:59:38
 * @Copyright Mission
 */

@Data
@TableName("t_ai_model_config")
public class AiModelConfigEntity {

    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 厂商标识 (方便前端展示回显，实际调用主要靠 baseUrl)
     */
    private String provider;

    /**
     * 关键配置
     */
    private String baseUrl;

    /**
     * API密钥
     */
    private String apiKey;

    /**
     * 模型名称
     */
    private String modelName;

    /**
     * 温度参数
     */
    private BigDecimal temperature;

    /**
     * 是否激活
     */
    private Integer isActive;

    /**
     * 输出响应最大令牌数
     */
    private Integer maxTokens;

    /**
     * 模型类型 (CHAT/EMBEDDING)
     */
    private String modelType;

    /**
     * Chat模型专用。附加到 Base URL 的路径。例如OpenAi的/v1/chat/completions
     */
    private String completionsPath;

    /**
     * 嵌入模型专用。附加到 Base URL 的路径。
     */
    private String embeddingsPath;

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

    /**
     * 0=未删除, 1=已删除
     */
    private Integer deletedFlag;

}
