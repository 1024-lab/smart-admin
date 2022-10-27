package net.lab1024.sa.admin.module.business.oa.notice.domain.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.sa.common.common.json.serializer.FileKeyVoSerializer;

import java.util.List;

/**
 * 用于更新 【通知、公告】 的 VO 对象
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-08-12 21:40:39
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ），2012-2022
 */
@Data
public class NoticeUpdateFormVO extends NoticeVO {

    @ApiModelProperty("纯文本内容")
    private String contentText;

    @ApiModelProperty("html内容")
    private String contentHtml;

    @ApiModelProperty("附件")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    private String attachment;

    @ApiModelProperty("可见范围")
    private List<NoticeVisibleRangeVO> visibleRangeList;

}
