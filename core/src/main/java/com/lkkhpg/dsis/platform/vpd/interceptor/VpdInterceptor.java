/*
 * #{copyright}#
 */

package com.lkkhpg.dsis.platform.vpd.interceptor;

import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lkkhpg.dsis.platform.vpd.parser.IVpdParser;
import com.lkkhpg.dsis.platform.vpd.sqlsource.VpdSqlSourceFactory;

/**
 * VPD拦截器.
 * @author chenjingxiong.
 */
@Intercepts(@Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
        RowBounds.class, ResultHandler.class }))
public class VpdInterceptor implements Interceptor {

    private static Logger logger = LoggerFactory.getLogger(VpdInterceptor.class);

    private IVpdParser vpdParser;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        MetaObject msObject = SystemMetaObject.forObject(ms);

        String mapperId = ms.getId();
        String mapperClass = getMapperClassName(mapperId);

        if (!vpdParser.canParse(mapperClass)) {
            return invocation.proceed();
        }

        if (logger.isInfoEnabled()) {
            logger.info("match vpd Rule: {}", mapperClass);
        }

        SqlSource sqlSource = ms.getSqlSource();

        VpdSqlSourceFactory factory = new VpdSqlSourceFactory(mapperClass, vpdParser);
        SqlSource vpdSqlSource = factory.covertSqlSource(sqlSource);

        msObject.setValue("sqlSource", vpdSqlSource);

        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }
    }

    @Override
    public void setProperties(Properties properties) {
    }

    public void setVpdParser(IVpdParser vpdParser) {
        this.vpdParser = vpdParser;
    }

    private String getMapperClassName(String mapperId) {
        if (!mapperId.endsWith("Mapper")) {
            mapperId = mapperId.substring(0, mapperId.lastIndexOf("."));
        }
        return mapperId;
    }

}
