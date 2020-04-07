package net.lab1024.smartadmin.module.system.department.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import net.lab1024.smartadmin.common.domain.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 部门实体类
 * t_department 数据表
 *
 * @author listen
 * @date 2017/12/19 10:45
 */
@Data
@TableName(value = "t_department")
public class DepartmentEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -6787726615141147044L;

    /**
     * 部门名称
     */
    private String name;

    /**
     * 部门简称
     */
    private String shortName;

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
    private Long sort;


}
