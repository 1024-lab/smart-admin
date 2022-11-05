package net.lab1024.sa.common.module.support.reload.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * reload结果 <br>
 * t_reload_result 数据表 实体类
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2015-03-02 19:11:52
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Data
@TableName("t_reload_result")
public class ReloadResultEntity {

    /**
     * 加载项标签
     */
    private String tag;

    /**
     * 运行标识
     */
    private String identification;

    /**
     * 参数
     */
    private String args;

    /**
     * 运行结果
     */
    private Boolean result;

    /**
     * 异常
     */
    private String exception;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;


}
