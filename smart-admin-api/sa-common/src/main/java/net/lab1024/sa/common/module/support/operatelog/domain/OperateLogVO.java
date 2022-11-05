package net.lab1024.sa.common.module.support.operatelog.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.sa.common.common.enumeration.UserTypeEnum;
import net.lab1024.sa.common.common.swagger.ApiModelPropertyEnum;

import java.time.LocalDateTime;
import java.util.Date;

/**
 *  操作日志信息
 *
 * @Author 1024创新实验室: 罗伊
 * @Date 2021-12-08 20:48:52
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Data
public class OperateLogVO {

    @ApiModelProperty("主键")
    private Long operateLogId;

    @ApiModelProperty("用户id")
    private Long operateUserId;

    @ApiModelPropertyEnum(value = UserTypeEnum.class, desc = "用户类型")
    private Integer operateUserType;

    @ApiModelProperty("用户名称")
    private String operateUserName;

    @ApiModelProperty("操作模块")
    private String module;

    @ApiModelProperty("操作内容")
    private String content;

    @ApiModelProperty("请求路径")
    private String url;

    @ApiModelProperty("请求方法")
    private String method;

    @ApiModelProperty("请求参数")
    private String param;

    @ApiModelProperty("客户ip")
    private String ip;

    @ApiModelProperty("user-agent")
    private String userAgent;

    @ApiModelProperty("请求结果 0失败 1成功")
    private Boolean successFlag;

    @ApiModelProperty("失败原因")
    private String failReason;

    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;


}
