package net.lab1024.sa.common.module.support.datatracer.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.sa.common.common.enumeration.UserTypeEnum;
import net.lab1024.sa.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.sa.common.module.support.datatracer.constant.DataTracerTypeEnum;

import java.time.LocalDateTime;

/**
 * 变动记录
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-07-23 19:38:52
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Data
public class DataTracerVO {

    @ApiModelProperty("日志id")
    private Long dataTracerId;

    @ApiModelProperty("单据id")
    private Long dataId;

    @ApiModelPropertyEnum(value = DataTracerTypeEnum.class, desc = "业务类型")
    private Integer type;

    @ApiModelProperty("操作内容")
    private String content;

    @ApiModelProperty("diff 差异：旧的数据")
    private String diffOld;

    @ApiModelProperty("差异：新的数据")
    private String diffNew;

    @ApiModelProperty("扩展字段")
    private String extraData;

    @ApiModelProperty("操作人")
    private Long userId;

    @ApiModelPropertyEnum(value = UserTypeEnum.class, desc = "用户类型")
    private Integer userType;

    @ApiModelProperty("操作人名称")
    private String userName;

    @ApiModelProperty("userAgent")
    private String userAgent;

    @ApiModelProperty("ip")
    private String ip;

    @ApiModelProperty("操作时间")
    private LocalDateTime createTime;

}
