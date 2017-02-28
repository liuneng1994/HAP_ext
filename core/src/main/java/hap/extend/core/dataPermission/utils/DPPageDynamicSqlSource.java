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

/**
 * DynamicSqlSource.
 * @author yyz
 */
public class DPPageDynamicSqlSource extends PageDynamicSqlSource {

    private Logger logger = LoggerFactory.getLogger(DPPageDynamicSqlSource.class);

    private Configuration configuration;
    private SqlNode rootSqlNode;
    private String conditionSql;

    public DPPageDynamicSqlSource(Configuration configuration, SqlNode rootSqlNode, String conditionSql) {
        this(new DynamicSqlSource(configuration, rootSqlNode));
        this.configuration = configuration;
        this.rootSqlNode = rootSqlNode;
        this.conditionSql = conditionSql;
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
        if(isNotNull(conditionSql)){
            String sql = boundSql.getSql();//old sql
            String newSql = null;
            try {
                newSql = SqlUtil.addConditionToSql(sql, conditionSql);
                newSql = SqlUtil.replaceLimit(newSql);
            } catch (JSQLParserException e) {
                e.printStackTrace();
                logger.error(e.getMessage(),e);
            }
            MetaObject metaObject = SystemMetaObject.forObject(boundSql);
            metaObject.setValue("sql", newSql);
        }
        return boundSql;
    }
}
