package net.lab1024.sa.admin.module.business.oa.notice.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.admin.module.business.oa.notice.constant.NoticeVisibleRangeDataTypeEnum;
import net.lab1024.sa.base.common.swagger.SchemaEnum;

/**
 * 新闻、公告 可见范围数据 VO
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-08-12 21:40:39
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
public class NoticeVisibleRangeVO {

    @SchemaEnum(NoticeVisibleRangeDataTypeEnum.class)
    private Integer dataType;

    @Schema(description = "员工/部门id")
    private Long dataId;

    @Schema(description = "员工/部门 名称")
    private String dataName;

}
