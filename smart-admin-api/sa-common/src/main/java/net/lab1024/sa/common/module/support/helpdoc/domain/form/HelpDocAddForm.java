package net.lab1024.sa.common.module.support.helpdoc.domain.form;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.sa.common.common.json.deserializer.FileKeyVoDeserializer;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 帮助文档
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-08-20 23:11:42
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Data
public class HelpDocAddForm {

    @ApiModelProperty("标题")
    @NotBlank(message = "标题不能为空")
    @Length(max = 200, message = "标题最多200字符")
    private String title;

    @ApiModelProperty("分类")
    @NotNull(message = "分类不能为空")
    private Long helpDocCatalogId;

    @ApiModelProperty("纯文本内容")
    @NotNull(message = "文本内容不能为空")
    private String contentText;

    @ApiModelProperty("html内容")
    @NotNull(message = "html内容不能为空")
    private String contentHtml;

    @ApiModelProperty("附件,多个英文逗号分隔|可选")
    @Length(max = 1000, message = "最多1000字符")
    @JsonDeserialize(using = FileKeyVoDeserializer.class)
    private String attachment;

    @ApiModelProperty("排序")
    @NotNull(message = "排序不能为空")
    private Integer sort;

    @ApiModelProperty("关联的集合")
    private List<HelpDocRelationForm> relationList;

    @ApiModelProperty("作者")
    @NotBlank(message = "作者不能为空")
    private String author;
}
