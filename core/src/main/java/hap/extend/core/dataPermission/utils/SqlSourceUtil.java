package hap.extend.core.dataPermission.utils;

import com.github.pagehelper.sqlsource.OrderByStaticSqlSource;
import com.github.pagehelper.sqlsource.PageDynamicSqlSource;
import com.github.pagehelper.sqlsource.PageRawSqlSource;
import com.github.pagehelper.sqlsource.PageStaticSqlSource;
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
import java.util.List;

import static hap.extend.core.dataPermission.utils.LangUtils.isNull;

/**
 * MyBatis SqlSource 工厂类.
 * 
 * @author yyz
 */
public final class SqlSourceUtil {

    private static Logger logger = LoggerFactory.getLogger(SqlSourceUtil.class);

    /**
     * 不同的sql source不同的处理模式.
     * @param sqlSource
     * @param conditionSql
     * @param parameterObject 查询参数对象，一般是dto，这个对象的拦截器的注解上已经注入了（实际上是query方法的第二个参数）
     * @return
     * @throws JSQLParserException
     */
    public static SqlSource covertSqlSource(SqlSource sqlSource, String conditionSql, Object parameterObject, ThreadLocal<String> threadLocal, ThreadLocal<Boolean> tlOfIsCountFlag) throws JSQLParserException {
        if(isNull(sqlSource)){
            return sqlSource;
        }

        SqlSource result = null;
        if (sqlSource instanceof RawSqlSource) {
            // 获取原始SQL
            MetaObject metaObject = SystemMetaObject.forObject(sqlSource);
            StaticSqlSource staticSqlSource = (StaticSqlSource) metaObject.getValue("sqlSource");
            MetaObject metaObject2 = SystemMetaObject.forObject(staticSqlSource);
            String oldSql = (String) metaObject2.getValue("sql");
            String newSql = SqlUtil.addConditionToSql(oldSql,conditionSql,tlOfIsCountFlag.get());
            // 替换新的SQL
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
            // 替换新的sql源
            DPDynamicSqlSource vpdDynamicSqlSource = new DPDynamicSqlSource(configuration, rootSqlNode, parameterObject, threadLocal, tlOfIsCountFlag);
            result = vpdDynamicSqlSource;
        } else if (sqlSource instanceof StaticSqlSource || sqlSource instanceof OrderByStaticSqlSource
                || sqlSource instanceof PageStaticSqlSource) {
            MetaObject metaObject = SystemMetaObject.forObject(sqlSource);
            String oldSql = (String) metaObject.getValue("sql");//old sql
            String newSql = SqlUtil.addConditionToSql(oldSql,conditionSql,tlOfIsCountFlag.get());
            // 替换新的sql
            metaObject.setValue("sql", newSql);

            result = sqlSource;
        } else if (sqlSource instanceof PageRawSqlSource) {
            MetaObject metaObject = SystemMetaObject.forObject(sqlSource);
            PageStaticSqlSource pageStaticSqlSource = (PageStaticSqlSource) metaObject.getValue("sqlSource");
            MetaObject metaObject2 = SystemMetaObject.forObject(pageStaticSqlSource);
            String oldSql = (String) metaObject2.getValue("sql");//old sql
            String newSql = SqlUtil.addConditionToSql(oldSql,conditionSql,tlOfIsCountFlag.get());
            //直接替换
            metaObject2.setValue("sql", newSql);

            result = sqlSource;
        } else if (sqlSource instanceof PageDynamicSqlSource) {
            MetaObject metaObject2 = SystemMetaObject.forObject(sqlSource);
            Configuration configuration = (Configuration) metaObject2.getValue("configuration");
            SqlNode rootSqlNode = (SqlNode) metaObject2.getValue("rootSqlNode");
//            DPPageDynamicSqlSource vpdPageDynamicSqlSource = new DPPageDynamicSqlSource(
//                    configuration, rootSqlNode,threadLocal, tlOfIsCountFlag);
            NewDPDynamicSqlSource vpdPageDynamicSqlSource = new NewDPDynamicSqlSource(
                    configuration, rootSqlNode,conditionSql);
            result = vpdPageDynamicSqlSource;
        }
        if (logger.isInfoEnabled()) {
            logger.info("covert sqlSource:{} to :{}", sqlSource, result);
        }

        return result;
    }
}