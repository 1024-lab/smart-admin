package net.lab1024.sa.base.module.support.operatelog.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 操作记录
 *
 * @Author 1024创新实验室: 罗伊
 * @Date 2021-12-08 20:48:52
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_operate_log")
public class OperateLogEntity {

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long operateLogId;

    /**
     * 操作人id
     */
    private Long operateUserId;

    /**
     * 用户类型
     */
    private Integer operateUserType;

    /**
     * 操作人名称
     */
    private String operateUserName;
    /**
     * 操作模块
     */
    private String module;

    /**
     * 操作内容
     */
    private String content;

    /**
     * 请求路径
     */
    private String url;

    /**
     * 请求方法
     */
    private String method;

    /**
     * 请求参数
     */
    private String param;

    /**
     * 客户ip
     */
    private String ip;

    /**
     * 客户ip地区
     */
    private String ipRegion;

    /**
     * user-agent
     */
    private String userAgent;

    /**
     * 请求结果 0失败 1成功
     */
    private Boolean successFlag;

    /**
     * 失败原因
     */
    private String failReason;


    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;


}
