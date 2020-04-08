package net.lab1024.smartadmin.module.support.smartreload.domain.dto;
import lombok.Data;

import java.util.Date;

/**
 * reload_result DTO 类
 *
 * @author listen
 * @date 2018/02/10 09:29
 */
@Data
public class ReloadResultVO {

    /**
     * 加载项标签
     */
    private String tag;

    /**
     * 参数
     */
    private String args;

    /**
     *  状态标识
     */
    private String identification;

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
    private Date createTime;

}
