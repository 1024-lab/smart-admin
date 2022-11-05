package net.lab1024.smartadmin.module.business.log.useroperatelog.domain;
import com.baomidou.mybatisplus.annotation.TableName;
import net.lab1024.smartadmin.common.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * [  ]
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2018 1024lab.netInc. All rights reserved.
 * @date 2019-05-15 11:32:14
 * @since JDK1.8
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_user_operate_log")
public class UserOperateLogEntity extends BaseEntity {



    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户名称
     */
    private String userName;
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
     * 请求结果 0失败 1成功
     */
    private Integer result;

    /**
     * 失败原因
     */
    private String failReason;



}
