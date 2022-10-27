package net.lab1024.sa.admin.module.business.oa.notice.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.sa.admin.module.business.oa.notice.constant.NoticeVisibleRangeDataTypeEnum;
import net.lab1024.sa.common.common.swagger.ApiModelPropertyEnum;

/**
 * 新闻、公告 可见范围数据 VO
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-08-12 21:40:39
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ），2012-2022
 */
@Data
public class NoticeVisibleRangeVO {

    @ApiModelPropertyEnum(NoticeVisibleRangeDataTypeEnum.class)
    private Integer dataType;

    @ApiModelProperty("员工/部门id")
    private Long dataId;

    @ApiModelProperty("员工/部门 名称")
    private String dataName;

}
