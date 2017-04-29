package hap.extend.core.dataPermission.utils;

import com.github.pagehelper.sqlsource.PageDynamicSqlSource;
import net.sf.jsqlparser.JSQLParserException;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.scripting.xmltags.DynamicSqlSource;
import org.apache.ibatis.scripting.xmltags.SqlNode;
import org.apache.ibatis.session.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static hap.extend.core.dataPermission.utils.LangUtils.isNotNull;
import static hap.extend.core.dataPermission.utils.SqlUtil.replaceLimit;

/**
 * DynamicSqlSource.
 * @author yyz
 */
@Deprecated
public class DPPageDynamicSqlSource extends PageDynamicSqlSource {

    private Logger logger = LoggerFactory.getLogger(DPPageDynamicSqlSource.class);

    private Configuration configuration;
    private SqlNode rootSqlNode;
    private ThreadLocal<String> tlOfConditionSql;
    private ThreadLocal<Boolean> tlOfIsCountFlag;

    public DPPageDynamicSqlSource(Configuration configuration, SqlNode rootSqlNode, ThreadLocal<String> threadLocal, ThreadLocal<Boolean> tlOfIsCountFlag) {
        this(new DynamicSqlSource(configuration, rootSqlNode));
        this.configuration = configuration;
        this.rootSqlNode = rootSqlNode;
        this.tlOfConditionSql = threadLocal;
        this.tlOfIsCountFlag = tlOfIsCountFlag;
    }

    /**
     * @param sqlSource sqlSource
     */
    public DPPageDynamicSqlSource(DynamicSqlSource sqlSource) {
        super(sqlSource);
    }

    @Override
    public BoundSql getBoundSql(Object parameterObject) {
        BoundSql boundSql = super.getBoundSql(parameterObject);
        String sql = boundSql.getSql();//old sql
        if(isNotNull(tlOfConditionSql) && isNotNull(tlOfConditionSql.get())){
            String newSql = null;
            try {
                newSql = SqlUtil.addConditionToSql(sql, tlOfConditionSql.get(), tlOfIsCountFlag.get());
                newSql = replaceLimit(newSql);
            } catch (JSQLParserException e) {
                e.printStackTrace();
                logger.error(e.getMessage(),e);
            }
            MetaObject metaObject = SystemMetaObject.forObject(boundSql);
            metaObject.setValue("sql", newSql);
        }else {
            MetaObject metaObject = SystemMetaObject.forObject(boundSql);
            metaObject.setValue("sql", sql);
        }
        return boundSql;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public SqlNode getRootSqlNode() {
        return rootSqlNode;
    }

    public ThreadLocal<String> getTlOfConditionSql() {
        return tlOfConditionSql;
    }

    public ThreadLocal<Boolean> getTlOfIsCountFlag() {
        return tlOfIsCountFlag;
    }
}
