package net.lab1024.sa.admin.module.business.oa.notice.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 通知公告
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-08-12 21:40:39
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@TableName("t_notice")
public class NoticeEntity {

    @TableId(type = IdType.AUTO)
    private Long noticeId;

    /**
     * 类型
     */
    private Long noticeTypeId;

    /**
     * 标题
     */
    private String title;

    /**
     * 是否全部可见
     */
    private Boolean allVisibleFlag;

    /**
     * 是否定时发布
     */
    private Boolean scheduledPublishFlag;

    /**
     * 发布时间
     */
    private LocalDateTime publishTime;

    /**
     * 内容 纯文本
     */
    private String contentText;

    /**
     * 内容 html
     */
    private String contentHtml;

    /**
     * 附件
     * 多个英文逗号分隔
     */
    private String attachment;

    /**
     * 页面浏览量
     */
    private Integer pageViewCount;

    /**
     * 用户浏览量
     */
    private Integer userViewCount;

    /**
     * 来源
     */
    private String source;

    /**
     * 作者
     */
    private String author;

    /**
     * 文号
     */
    private String documentNumber;

    private Boolean deletedFlag;

    private Long createUserId;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;
}
