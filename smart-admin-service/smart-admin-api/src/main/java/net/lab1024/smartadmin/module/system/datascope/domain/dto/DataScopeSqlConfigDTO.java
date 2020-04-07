package net.lab1024.smartadmin.module.system.datascope.domain.dto;

import lombok.Data;

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

    private Integer dataScopeType;

    private String joinSql;

    private Integer whereIndex;

    private Integer dataScopeWhereInType;
}
