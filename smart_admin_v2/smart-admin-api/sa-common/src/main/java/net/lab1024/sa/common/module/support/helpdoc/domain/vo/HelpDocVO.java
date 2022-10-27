package net.lab1024.sa.common.module.support.helpdoc.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

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
public class HelpDocVO {

    @ApiModelProperty("id")
    private Long helpDocId;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("分类")
    private Long helpDocCatalogId;

    @ApiModelProperty("分类名称")
    private String helpDocCatalogName;

    @ApiModelProperty("作者")
    private String author;
    
    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("页面浏览量")
    private Integer pageViewCount;

    @ApiModelProperty("用户浏览量")
    private Integer userViewCount;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

}
