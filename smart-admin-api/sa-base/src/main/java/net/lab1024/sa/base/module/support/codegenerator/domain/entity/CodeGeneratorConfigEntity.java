package net.lab1024.sa.base.module.support.codegenerator.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 代码生成-配置
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022/6/23 21:59:22
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@TableName("t_code_generator_config")
public class CodeGeneratorConfigEntity {

    /**
     * 表名
     */
    @TableId(type = IdType.NONE)
    private String tableName;

    /**
     * 基础命名信息
     */
    private String basic;

    /**
     * 字段列表
     */
    private String fields;

    /**
     * 增加、修改 信息
     */
    private String insertAndUpdate;

    /**
     * 删除 信息
     */
    private String deleteInfo;

    /**
     * 查询字段
     */
    private String queryFields;

    /**
     * 列表字段
     */
    private String tableFields;

    /**
     * 详情
     */
    private String detail;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
