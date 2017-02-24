/*
 * #{copyright}#
 */
package com.lkkhpg.dsis.platform.vpd.sqlsource;

import net.sf.jsqlparser.JSQLParserException;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.scripting.xmltags.DynamicSqlSource;
import org.apache.ibatis.scripting.xmltags.SqlNode;
import org.apache.ibatis.session.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.pagehelper.sqlsource.PageDynamicSqlSource;
import com.lkkhpg.dsis.platform.vpd.exception.VpdParseErrorException;
import com.lkkhpg.dsis.platform.vpd.parser.IVpdParser;

/**
 * DynamicSqlSource.
 * @author chenjingxiong
 */
public class VpdPageDynamicSqlSource extends PageDynamicSqlSource {
    
    private Logger logger = LoggerFactory.getLogger(VpdPageDynamicSqlSource.class);

    private Configuration configuration;

    private SqlNode rootSqlNode;

    private String mapperId;

    private IVpdParser vpdParser;

    public VpdPageDynamicSqlSource(Configuration configuration, SqlNode rootSqlNode, String mapperId,
            IVpdParser vpdParser) {
        this(new DynamicSqlSource(configuration, rootSqlNode));
        this.configuration = configuration;
        this.rootSqlNode = rootSqlNode;
        this.mapperId = mapperId;
        this.vpdParser = vpdParser;
    }

    /**
     * @param sqlSource sqlSource
     */
    public VpdPageDynamicSqlSource(DynamicSqlSource sqlSource) {
        super(sqlSource);
    }

    @Override
    public BoundSql getBoundSql(Object parameterObject) {
        BoundSql boundSql = super.getBoundSql(parameterObject);
        String sql = boundSql.getSql();
        String newSql = sql;
        try {
            newSql = this.vpdParser.parseAndBuild(mapperId, sql);
        } catch (VpdParseErrorException | JSQLParserException e) {
            if (logger.isErrorEnabled()) {
                logger.error(e.getMessage(), e);
            }
            throw new RuntimeException(e);
        }
        MetaObject metaObject = SystemMetaObject.forObject(boundSql);
        metaObject.setValue("sql", newSql);

        return boundSql;
    }

}
