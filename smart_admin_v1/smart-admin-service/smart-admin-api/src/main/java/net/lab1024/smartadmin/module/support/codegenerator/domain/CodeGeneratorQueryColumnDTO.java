package net.lab1024.smartadmin.module.support.codegenerator.domain;

import net.lab1024.smartadmin.module.support.codegenerator.constant.SqlOperateTypeEnum;
import lombok.Builder;
import lombok.Data;

/**
 * [  ]
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2018 1024lab.netInc. All rights reserved.
 * @date 2019/5/11 0011 上午 10:13
 * @since JDK1.8
 */
@Data
@Builder
public class CodeGeneratorQueryColumnDTO {

    /**
     * 生成查询方法的查询列名
     */
    private String columnName;

    /**
     * 此列的查询动作
     */
    private SqlOperateTypeEnum sqlOperate;
}
