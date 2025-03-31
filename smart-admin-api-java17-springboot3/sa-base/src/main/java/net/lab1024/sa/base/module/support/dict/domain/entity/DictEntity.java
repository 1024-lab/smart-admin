package net.lab1024.sa.base.module.support.dict.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 数据字典 实体类
 *
 * @Author 1024创新实验室-主任-卓大
 * @Date 2025-03-25 22:25:04
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */

@Data
@TableName("t_dict")
public class DictEntity {

    /**
     * 字典id
     */
    @TableId(type = IdType.AUTO)
    private Long dictId;

    /**
     * 字典名字
     */
    private String dictName;

    /**
     * 字典编码
     */
    private String dictCode;

    /**
     * 字典备注
     */
    private String remark;

    /**
     * 禁用状态
     */
    private Boolean disabledFlag;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
