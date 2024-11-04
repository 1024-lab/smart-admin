package net.lab1024.sa.base.module.support.dict.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 字典
 *
 * @Author 1024创新实验室: 罗伊
 * @Date 2022/5/26 19:40:55
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@TableName("t_dict_value")
public class DictValueEntity {

    @TableId(type = IdType.AUTO)
    private Long dictValueId;

    private Long dictKeyId;
    /**
     * 编码
     */
    private String valueCode;
    /**
     * 名称
     */
    private String valueName;
    /**
     * 备注
     */
    private String remark;

    /**
     * 排序
     */
    private Integer sort;
    /**
     * 删除标识
     */
    private Boolean deletedFlag;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}