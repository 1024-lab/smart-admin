package net.lab1024.sa.common.module.support.feedback.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.sa.common.common.enumeration.UserTypeEnum;
import net.lab1024.sa.common.common.json.deserializer.FileKeyVoDeserializer;
import net.lab1024.sa.common.common.json.serializer.FileKeyVoSerializer;
import net.lab1024.sa.common.common.swagger.ApiModelPropertyEnum;

import java.time.LocalDateTime;

/**
 * 意见反馈 返回对象
 *
 * @Author 1024创新实验室: 开云
 * @Date 2022-08-11 20:48:09
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Data
public class FeedbackVO {

    @ApiModelProperty(value = "主键")
    private Long feedbackId;

    @ApiModelProperty(value = "反馈内容")
    private String feedbackContent;

    @ApiModelProperty("反馈图片")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    @JsonDeserialize(using = FileKeyVoDeserializer.class)
    private String feedbackAttachment;

    @ApiModelProperty(value = "创建人id")
    private Long userId;

    @ApiModelProperty(value = "创建人姓名")
    private String userName;

    @ApiModelPropertyEnum(value = UserTypeEnum.class, desc = "创建人类型")
    private Integer userType;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
}