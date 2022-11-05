package net.lab1024.sa.admin.module.business.oa.notice.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.sa.common.common.domain.PageParam;

import java.time.LocalDate;

/**
 * 通知公告 员工查询表单
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-08-12 21:40:39
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ），2012-2022
 */
@Data
public class NoticeEmployeeQueryForm extends PageParam {

    @ApiModelProperty("标题、作者、来源、文号")
    private String keywords;

    @ApiModelProperty("分类")
    private Long noticeTypeId;

    @ApiModelProperty("发布-开始时间")
    private LocalDate publishTimeBegin;

    @ApiModelProperty("未读标识")
    private Boolean notViewFlag;

    @ApiModelProperty("发布-截止时间")
    private LocalDate publishTimeEnd;
}
