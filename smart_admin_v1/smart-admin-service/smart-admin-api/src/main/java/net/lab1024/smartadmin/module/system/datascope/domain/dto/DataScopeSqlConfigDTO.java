package net.lab1024.smartadmin.module.system.datascope.domain.dto;

import lombok.Data;
import net.lab1024.smartadmin.module.system.datascope.constant.DataScopeTypeEnum;
import net.lab1024.smartadmin.module.system.datascope.constant.DataScopeWhereInTypeEnum;

/**
 * [  ]
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2018 1024lab.netInc. All rights reserved.
 * @date 2019/4/28 0028 下午 17:21
 * @since JDK1.8
 */
@Data
public class DataScopeSqlConfigDTO {

    /**
     * 数据范围类型
     * {@link DataScopeTypeEnum}
     */
    private DataScopeTypeEnum dataScopeType;

    /**
     * join sql 具体实现类
     */
    private Class joinSqlImplClazz;

    private String joinSql;

    private Integer whereIndex;

    /**
     * whereIn类型
     * {@link DataScopeWhereInTypeEnum}
     */
    private DataScopeWhereInTypeEnum dataScopeWhereInType;
}
