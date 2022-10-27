package net.lab1024.sa.common.module.support.config.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 系统配置参数 实体类
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-03-14 20:46:27
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Data
@TableName("t_config")
public class ConfigEntity {

    @TableId(type = IdType.AUTO)
    private Long configId;

    /**
     * 参数key
     */
    private String configKey;

    /**
     * 参数的值
     */
    private String configValue;

    /**
     * 参数名称
     */
    private String configName;

    /**
     * 备注
     */
    private String remark;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;
}
