package net.lab1024.smartadmin.module.business.email.domain.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import net.lab1024.smartadmin.common.domain.BaseEntity;
import lombok.Data;

/**
 * [  ]
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2018 1024lab.netInc. All rights reserved.
 * @date 2019-05-13 17:10:16
 * @since JDK1.8
 */
@Data
@TableName("t_email")
public class EmailEntity extends BaseEntity {

    /**
     * 标题
     */
    private String title;

    /**
     * 收件人
     */
    private String toEmails;

    /**
     * 发送状态 0未发送 1已发送
     */
    private Integer sendStatus;

    /**
     * 邮件内容
     */
    private String content;

}
