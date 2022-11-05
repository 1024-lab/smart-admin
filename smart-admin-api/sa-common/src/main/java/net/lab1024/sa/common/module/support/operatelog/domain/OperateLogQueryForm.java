package net.lab1024.sa.common.module.support.operatelog.domain;

import net.lab1024.sa.common.common.domain.PageParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *  操作日志查询 表单
 *
 * @Author 1024创新实验室: 罗伊
 * @Date 2021-12-08 20:48:52
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Data
public class OperateLogQueryForm extends PageParam {


    @ApiModelProperty("开始日期")
    private String startDate;

    @ApiModelProperty("结束日期")
    private String endDate;


    @ApiModelProperty("用户名称")
    private String userName;

    @ApiModelProperty("请求结果 false失败 true成功")
    private Boolean successFlag;

}
