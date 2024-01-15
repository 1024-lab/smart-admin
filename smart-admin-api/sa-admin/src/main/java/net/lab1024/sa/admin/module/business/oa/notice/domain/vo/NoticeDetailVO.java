package net.lab1024.sa.admin.module.business.oa.notice.domain.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.base.common.json.serializer.FileKeyVoSerializer;

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
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
public class NoticeDetailVO {

    @Schema(description = "id")
    private Long noticeId;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "分类")
    private Long noticeTypeId;

    @Schema(description = "分类名称")
    private Long noticeTypeName;

    @Schema(description = "是否全部可见")
    @NotNull(message = "是否全部可见不能为空")
    private Boolean allVisibleFlag;

    @Schema(description = "是否定时发布")
    @NotNull(message = "是否定时发布不能为空")
    private Boolean scheduledPublishFlag;

    @Schema(description = "纯文本内容")
    private String contentText;

    @Schema(description = "html内容")
    private String contentHtml;

    @Schema(description = "附件")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    private String attachment;

    @Schema(description = "发布时间")
    @NotNull(message = "发布时间不能为空")
    private LocalDateTime publishTime;

    @Schema(description = "作者")
    @NotBlank(message = "作者不能为空")
    private String author;

    @Schema(description = "来源")
    @NotBlank(message = "标题不能为空")
    private String source;

    @Schema(description = "文号")
    private String documentNumber;

    @Schema(description = "页面浏览量")
    private Integer pageViewCount;

    @Schema(description = "用户浏览量")
    private Integer userViewCount;

    @Schema(description = "创建人名称")
    private Long createUserName;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

}
