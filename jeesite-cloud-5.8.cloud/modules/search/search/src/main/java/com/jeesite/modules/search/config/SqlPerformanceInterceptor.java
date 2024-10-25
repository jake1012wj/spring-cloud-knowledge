package com.jeesite.modules.search.config;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Statement;

@Intercepts({
    @Signature(type = StatementHandler.class, method = "prepare", args = {Statement.class})
})
public class SqlPerformanceInterceptor implements Interceptor {
    private static final Logger logger = LoggerFactory.getLogger(SqlPerformanceInterceptor.class);

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = invocation.proceed();
        long end = System.currentTimeMillis();
        logger.info("SQL executed in {} ms", (end - start));
        return result;
    }
}
