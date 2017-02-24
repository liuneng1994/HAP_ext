/*
 * #{copyright}#
 */
package com.lkkhpg.dsis.platform.vpd.sqlsource;

import java.util.List;

import net.sf.jsqlparser.JSQLParserException;

import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.scripting.defaults.RawSqlSource;
import org.apache.ibatis.scripting.xmltags.DynamicSqlSource;
import org.apache.ibatis.scripting.xmltags.SqlNode;
import org.apache.ibatis.session.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.pagehelper.sqlsource.OrderByStaticSqlSource;
import com.github.pagehelper.sqlsource.PageDynamicSqlSource;
import com.github.pagehelper.sqlsource.PageRawSqlSource;
import com.github.pagehelper.sqlsource.PageStaticSqlSource;
import com.lkkhpg.dsis.platform.vpd.exception.VpdParseErrorException;
import com.lkkhpg.dsis.platform.vpd.parser.IVpdParser;

/**
 * VPD MyBatis SqlSource 工厂类.
 * 
 * @author chenjingxiong
 */
public class VpdSqlSourceFactory {

    private static Logger logger = LoggerFactory.getLogger(VpdSqlSourceFactory.class);

    private String mapperId;

    private IVpdParser vpdParser;

    public VpdSqlSourceFactory(String mapperId, IVpdParser vpdParser) {
        this.mapperId = mapperId;
        this.vpdParser = vpdParser;
    }

    /**
     * 不同的sqlsource不同的处理模式.
     * 
     * @param sqlSource
     *            sqlSource
     * @return SqlSource SqlSource
     * @throws JSQLParserException
     *             JSQLParserException
     * @throws VpdParseErrorException
     *             VpdParseErrorException
     */
    public SqlSource covertSqlSource(SqlSource sqlSource) throws VpdParseErrorException, JSQLParserException {
        SqlSource result = null;
        if (sqlSource instanceof RawSqlSource) {
            // 获取原始SQL
            MetaObject metaObject = SystemMetaObject.forObject(sqlSource);
            StaticSqlSource staticSqlSource = (StaticSqlSource) metaObject.getValue("sqlSource");
            MetaObject metaObject2 = SystemMetaObject.forObject(staticSqlSource);
            String sql = (String) metaObject2.getValue("sql");

            // 根据VPD规则重新构建SQL
            String newSql = vpdParser.parseAndBuild(mapperId, sql);

            Configuration configuration = (Configuration) metaObject2.getValue("configuration");
            List<ParameterMapping> parameterMappings = (List<ParameterMapping>) metaObject2
                    .getValue("parameterMappings");

            StaticSqlSource vpdSqlSource = new StaticSqlSource(configuration, newSql, parameterMappings);

            metaObject.setValue("sqlSource", vpdSqlSource);

            result = sqlSource;
        } else if (sqlSource instanceof DynamicSqlSource) {
            MetaObject metaObject2 = SystemMetaObject.forObject(sqlSource);
            Configuration configuration = (Configuration) metaObject2.getValue("configuration");
            SqlNode rootSqlNode = (SqlNode) metaObject2.getValue("rootSqlNode");
            // 根据VPD规则重新构建SQL
            VpdDynamicSqlSource vpdDynamicSqlSource = new VpdDynamicSqlSource(configuration, rootSqlNode, mapperId,
                    vpdParser);

            return vpdDynamicSqlSource;
        } else if (sqlSource instanceof StaticSqlSource || sqlSource instanceof OrderByStaticSqlSource
                || sqlSource instanceof PageStaticSqlSource) {
            MetaObject metaObject = SystemMetaObject.forObject(sqlSource);
            String sql = (String) metaObject.getValue("sql");
            // 根据VPD规则重新构建SQL
            String newSql = vpdParser.parseAndBuild(mapperId, sql);
            metaObject.setValue("sql", newSql);

            result = sqlSource;
        } else if (sqlSource instanceof PageRawSqlSource) {
            MetaObject metaObject = SystemMetaObject.forObject(sqlSource);
            PageStaticSqlSource pageStaticSqlSource = (PageStaticSqlSource) metaObject.getValue("sqlSource");
            MetaObject metaObject2 = SystemMetaObject.forObject(pageStaticSqlSource);
            String sql = (String) metaObject2.getValue("sql");
            // 根据VPD规则重新构建SQL
            String newSql = vpdParser.parseAndBuild(mapperId, sql);
            metaObject2.setValue("sql", newSql);

            result = sqlSource;
        } else if (sqlSource instanceof PageDynamicSqlSource) {
            MetaObject metaObject2 = SystemMetaObject.forObject(sqlSource);
            Configuration configuration = (Configuration) metaObject2.getValue("configuration");
            SqlNode rootSqlNode = (SqlNode) metaObject2.getValue("rootSqlNode");
            VpdPageDynamicSqlSource vpdPageDynamicSqlSource = new VpdPageDynamicSqlSource(
                    configuration, rootSqlNode, mapperId, vpdParser);
            result = vpdPageDynamicSqlSource;
        }
        if (logger.isInfoEnabled()) {
            logger.info("covert sqlSource:{} to :{}", sqlSource, result);
        }
        return result;
    }
}
