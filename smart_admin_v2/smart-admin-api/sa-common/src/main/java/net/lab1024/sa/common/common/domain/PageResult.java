package net.lab1024.sa.common.common.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 分页返回对象
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2020/04/28 16:19
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Data
public class PageResult<T> {

    /**
     * 当前页
     */
    @ApiModelProperty(value = "当前页")
    private Long pageNum;

    /**
     * 每页的数量
     */
    @ApiModelProperty(value = "每页的数量")
    private Long pageSize;

    /**
     * 总记录数
     */
    @ApiModelProperty(value = "总记录数")
    private Long total;

    /**
     * 总页数
     */
    @ApiModelProperty(value = "总页数")
    private Long pages;

    /**
     * 结果集
     */
    @ApiModelProperty(value = "结果集")
    private List<T> list;

    @ApiModelProperty("是否为空")
    private Boolean emptyFlag;

}
