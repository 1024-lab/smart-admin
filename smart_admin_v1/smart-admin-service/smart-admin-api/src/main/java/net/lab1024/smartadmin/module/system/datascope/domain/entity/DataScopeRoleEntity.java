package net.lab1024.smartadmin.module.system.datascope.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import net.lab1024.smartadmin.common.domain.BaseEntity;
import lombok.Data;

/**
 * [ 数据范围与角色关系 ]
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2018 1024lab.netInc. All rights reserved.
 * @date 2019/4/27 0027 下午 14:43
 * @since JDK1.8
 */
@Data
@TableName("t_role_data_scope")
public class DataScopeRoleEntity extends BaseEntity {

    /**
     * 数据范围id
     */
    private Integer dataScopeType;
    /**
     * 数据范围类型
     */
    private Integer viewType;
    /**
     * 角色id
     */
    private Long roleId;
}
