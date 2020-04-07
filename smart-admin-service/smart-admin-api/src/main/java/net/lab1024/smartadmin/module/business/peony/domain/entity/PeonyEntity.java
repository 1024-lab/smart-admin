package net.lab1024.smartadmin.module.business.peony.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import net.lab1024.smartadmin.common.domain.BaseEntity;
import lombok.Data;

/**
 * [ 牡丹花 ]
 *
 * @author 卓大
 * @version 1.0
 * @company 1024创新实验室( www.1024lab.net )
 * @copyright (c)  1024创新实验室( www.1024lab.net )Inc. All rights reserved.
 * @date 2020-04-06 18:17:56
 * @since JDK1.8
 */
@Data
@TableName("t_peony")
public class PeonyEntity extends BaseEntity{


    /**
     * 品种
     */
    private String kind;

    /**
     * 名字
     */
    private String name;

    /**
     * 颜色
     */
    private String color;

    /**
     * 图片链接
     */
    private String imageUrl;



}
