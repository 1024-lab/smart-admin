package net.lab1024.smartadmin.module.business.notice.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * [  ]
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2018 1024lab.netInc. All rights reserved.
 * @date 2019/3/27 0027 下午 12:27
 * @since JDK1.8
 */
@Data
public class NoticeAddDTO {

    @ApiModelProperty("消息标题")
    @Length(max = 200)
    private String title;

    @ApiModelProperty("消息内容")
    @Length(max = 5000)
    private String content;

}
