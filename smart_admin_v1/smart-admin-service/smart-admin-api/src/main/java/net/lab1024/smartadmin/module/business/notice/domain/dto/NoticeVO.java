package net.lab1024.smartadmin.module.business.notice.domain.dto;
import net.lab1024.smartadmin.common.anno.ApiModelPropertyEnum;
import net.lab1024.smartadmin.common.constant.JudgeEnum;
import lombok.Data;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

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
public class NoticeVO {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("消息标题")
    private String title;


    @ApiModelProperty("消息创建人")
    private Long createUser;

    @ApiModelPropertyEnum(enumDesc = "发送状态",value = JudgeEnum.class)
    private Integer sendStatus;

    @ApiModelProperty("消息创建人名称")
    private String createUserName;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

}
