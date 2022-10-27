package net.lab1024.smartadmin.module.business.peony.domain.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;
import java.util.Date;

/**
 *  [ 牡丹花 ]
 *
 * @author 卓大
 * @version 1.0
 * @company 1024创新实验室( www.1024lab.net )
 * @copyright (c) 1024创新实验室( www.1024lab.net )Inc. All rights reserved.
 * @date  2020-04-06 18:17:56
 * @since JDK1.8
 */
@Data
public class PeonyExcelVO {
    @Excel(name = "ID")
    private Long id;

    @Excel(name = "品种")
    private String kind;

    @Excel(name = "名字")
    private String name;

    @Excel(name = "颜色")
    private String color;

    @Excel(name = "图片链接")
    private String imageUrl;

    @Excel(name = "创建时间", format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Excel(name = "更新时间", format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;



}
