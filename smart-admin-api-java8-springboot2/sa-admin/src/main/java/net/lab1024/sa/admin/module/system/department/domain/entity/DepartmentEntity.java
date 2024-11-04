package net.lab1024.sa.admin.module.system.department.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 部门实体类
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-01-12 20:37:48
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@TableName(value = "t_department")
public class DepartmentEntity {

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long departmentId;

    /**
     * 部门名称
     */
    private String name;

    /**
     * 负责人员工 id
     */
    private Long managerId;

    /**
     * 部门父级id
     */
    private Long parentId;

    /**
     * 排序
     */
    private Integer sort;


    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;



}
