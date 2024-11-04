package net.lab1024.sa.base.module.support.helpdoc.domain.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.base.common.json.serializer.FileKeyVoSerializer;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 帮助文档 详情
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-08-20 23:11:42
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
public class HelpDocDetailVO {

    @Schema(description = "id")
    private Long helpDocId;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "分类")
    private Long helpDocCatalogId;

    @Schema(description = "分类名称")
    private Long helpDocCatalogName;

    @Schema(description = "纯文本内容")
    private String contentText;

    @Schema(description = "html内容")
    private String contentHtml;

    @Schema(description = "附件")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    private String attachment;

    @Schema(description = "作者")
    @NotBlank(message = "作者不能为空")
    private String author;

    @Schema(description = "页面浏览量")
    private Integer pageViewCount;

    @Schema(description = "用户浏览量")
    private Integer userViewCount;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @Schema(description = "关联项目")
    private List<HelpDocRelationVO> relationList;

}
