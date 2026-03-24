package net.lab1024.sa.admin.module.business.ai.model.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 厂商标识 (方便前端展示回显，实际调用主要靠 baseUrl)
     */
    private String provider;

    /**
     * BaseUrl
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
     * 是否启用
     */
    private Integer isActive;

    /**
     * 请求端点中模型名称
     */
    private String endPointModeName;

    /**
     * 输出响应最大令牌数
     */
    private Integer maxTokens;

    /**
     * 模型类型: chat/embedding/image_gen/video_gen/rerank/tts/stt
     */
    private String modelType;

    /**
     * 能力标签（逗号分隔，例如 VISION,FUNCTION_CALL）
     */
    private String modelFeatures;

    /**
     * 提交路径
     */
    private String path;

    /**
     * 扩展配置JSON
     */
    private String extraConfigJson;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 创建人姓名
     */
    @TableField(fill = FieldFill.INSERT)
    private String createByName;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /**
     * 更新人姓名
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateByName;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 0=未删除, 1=已删除
     */
    private Integer deletedFlag;

}
