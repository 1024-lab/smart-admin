package net.lab1024.sa.admin.module.business.ai.merch.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_ai_merch_task")
public class AiMerchTaskEntity {

    @TableId(value = "task_id", type = IdType.ASSIGN_ID)
    private Long taskId;

    private Long sourceFileId;

    private String sourceFileKey;

    private String promptTemplate;

    private Long promptModelId;

    private String promptModelName;

    private Long imageModelId;

    private String imageModelName;

    private Long videoModelId;

    private String videoModelName;

    private String generatedPrompt;

    private String promptStatus;

    private String imageStatus;

    private String videoStatus;

    private String imageTaskId;

    private String videoTaskId;

    private String imageResultUrl;

    private String videoResultUrl;

    private Long imageFileId;

    private String imageFileKey;

    private Long videoFileId;

    private String videoFileKey;

    private String promptErrorMsg;

    private String imageErrorMsg;

    private String videoErrorMsg;

    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @TableField(fill = FieldFill.INSERT)
    private String createByName;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateByName;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    private Integer deletedFlag;
}
