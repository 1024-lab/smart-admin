package net.lab1024.sa.admin.module.business.oa.notice.domain.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.sa.common.common.json.serializer.FileKeyVoSerializer;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 通知公告 详情
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-08-12 21:40:39
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ），2012-2022
 */
@Data
public class NoticeDetailVO {

    @ApiModelProperty("id")
    private Long noticeId;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("分类")
    private Long noticeTypeId;

    @ApiModelProperty("分类名称")
    private Long noticeTypeName;

    @ApiModelProperty("是否全部可见")
    @NotNull(message = "是否全部可见不能为空")
    private Boolean allVisibleFlag;

    @ApiModelProperty("是否定时发布")
    @NotNull(message = "是否定时发布不能为空")
    private Boolean scheduledPublishFlag;

    @ApiModelProperty("纯文本内容")
    private String contentText;

    @ApiModelProperty("html内容")
    private String contentHtml;

    @ApiModelProperty("附件")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    private String attachment;

    @ApiModelProperty("发布时间")
    @NotNull(message = "发布时间不能为空")
    private LocalDateTime publishTime;

    @ApiModelProperty("作者")
    @NotBlank(message = "作者不能为空")
    private String author;

    @ApiModelProperty("来源")
    @NotBlank(message = "标题不能为空")
    private String source;

    @ApiModelProperty("文号")
    private String documentNumber;

    @ApiModelProperty("页面浏览量")
    private Integer pageViewCount;

    @ApiModelProperty("用户浏览量")
    private Integer userViewCount;

    @ApiModelProperty("创建人名称")
    private Long createUserName;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

}
