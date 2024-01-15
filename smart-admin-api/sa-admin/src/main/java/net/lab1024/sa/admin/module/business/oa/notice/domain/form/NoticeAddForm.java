package net.lab1024.sa.admin.module.business.oa.notice.domain.form;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.base.common.json.deserializer.FileKeyVoDeserializer;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 通知公告 添加表单
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-08-12 21:40:39
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
public class NoticeAddForm {

    @Schema(description = "标题")
    @NotBlank(message = "标题不能为空")
    @Length(max = 200, message = "标题最多200字符")
    private String title;

    @Schema(description = "分类")
    @NotNull(message = "分类不能为空")
    private Long noticeTypeId;

    @Schema(description = "是否全部可见")
    @NotNull(message = "是否全部可见不能为空")
    private Boolean allVisibleFlag;

    @Schema(description = "是否定时发布")
    @NotNull(message = "是否定时发布不能为空")
    private Boolean scheduledPublishFlag;

    @Schema(description = "发布时间")
    @NotNull(message = "发布时间不能为空")
    private LocalDateTime publishTime;

    @Schema(description = "纯文本内容")
    @NotNull(message = "文本内容不能为空")
    private String contentText;

    @Schema(description = "html内容")
    @NotNull(message = "html内容不能为空")
    private String contentHtml;

    @Schema(description = "附件,多个英文逗号分隔|可选")
    @Length(max = 1000, message = "最多1000字符")
    @JsonDeserialize(using = FileKeyVoDeserializer.class)
    private String attachment;

    @Schema(description = "作者")
    @NotBlank(message = "作者不能为空")
    private String author;

    @Schema(description = "来源")
    @NotBlank(message = "标题不能为空")
    private String source;

    @Schema(description = "文号")
    private String documentNumber;

    @Schema(hidden = true)
    private Long createUserId;

    @Schema(description = "可见范围设置|可选")
    @Valid
    private List<NoticeVisibleRangeForm> visibleRangeList;
}
