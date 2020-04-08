package net.lab1024.smartadmin.common.mybatis;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;

import java.util.Properties;

/**
 * [ mybaits sql 拦截 ]
 *
 * @author zhuoda
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2019 1024lab.netInc. All rights reserved.
 * @date
 * @since JDK1.8
 */
@Intercepts({@Signature(type = org.apache.ibatis.executor.Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
@Slf4j
public class MyBatisSqlUpdateSqlDebugPlugin implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        Object parameter = invocation.getArgs()[1];
        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
        log.info(boundSql.getSql()+"\r\n"+boundSql.getParameterMappings().toString());
        Object obj = invocation.proceed();
        return obj;
    }

    @Override
    public Object plugin(Object arg0) {
        return Plugin.wrap(arg0,this);
    }

    @Override
    public void setProperties(Properties arg0) {

    }

}