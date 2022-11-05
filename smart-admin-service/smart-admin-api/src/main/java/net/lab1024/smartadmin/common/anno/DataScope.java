package net.lab1024.smartadmin.common.anno;

import net.lab1024.smartadmin.module.system.datascope.constant.DataScopeTypeEnum;
import net.lab1024.smartadmin.module.system.datascope.constant.DataScopeWhereInTypeEnum;
import net.lab1024.smartadmin.module.system.datascope.strategy.DataScopePowerStrategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * [ 数据范围 ]
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2019 1024lab.netInc. All rights reserved.
 * @date
 * @since JDK1.8
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DataScope {

    DataScopeTypeEnum dataScopeType() default DataScopeTypeEnum.DEFAULT;

    DataScopeWhereInTypeEnum whereInType() default DataScopeWhereInTypeEnum.EMPLOYEE;

    /**
     * DataScopeWhereInTypeEnum.CUSTOM_STRATEGY类型 才可使用joinSqlImplClazz属性
     * @return
     */
    Class<? extends DataScopePowerStrategy> joinSqlImplClazz()  default DataScopePowerStrategy.class;

    /**
     *
     * 第几个where 条件 从0开始
     * @return
     */
    int whereIndex() default 0;

    /**
     * DataScopeWhereInTypeEnum为CUSTOM_STRATEGY类型时，此属性无效
     * @return
     */
    String joinSql() default "";

}
