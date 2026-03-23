package net.lab1024.sa.base.module.support.mail.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 *
 * 邮件模板
 *
 * @Author 1024创新实验室-创始人兼主任:卓大
 * @Date 2024/8/5
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a> ，Since 2012
 */
@Data
@TableName("t_mail_template")
public class MailTemplateEntity {

    @TableId(type = IdType.NONE)
    private String templateCode;

    /**
     * 主题
     */
    private String templateSubject;

    /**
     * 模板类型
     */
    private String templateType;

    /**
     * 模板内容
     */
    private String templateContent;

    /**
     * 禁用标识
     */
    private Boolean disableFlag;


    private LocalDateTime updateTime;

    private LocalDateTime createTime;
}
