package net.lab1024.sa.base.module.support.reload.core.domain;

import lombok.Data;

/**
 * t_reload_result 表 实体类
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2015-03-02 19:11:52
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
public class SmartReloadResult {

    /**
     * 项名称
     */
    private String tag;

    /**
     * 参数
     */
    private String args;

    /**
     * 标识
     */
    private String identification;

    /**
     * 处理结果
     */
    private boolean result;

    /**
     * 异常说明
     */
    private String exception;


}
