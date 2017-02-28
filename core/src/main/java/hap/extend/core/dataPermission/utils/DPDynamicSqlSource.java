package hap.extend.core.dataPermission.utils;

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
public class DPDynamicSqlSource extends DynamicSqlSource {

    private Logger logger = LoggerFactory.getLogger(DPDynamicSqlSource.class);

    private Configuration configuration;

    private SqlNode rootSqlNode;
    /** 条件SQL片段*/
    private String conditionSql;

    public DPDynamicSqlSource(Configuration configuration, SqlNode rootSqlNode,String conditionSql) {
        super(configuration, rootSqlNode);
        this.configuration = configuration;
        this.rootSqlNode = rootSqlNode;
        this.conditionSql = conditionSql;
    }

    @Override
    public BoundSql getBoundSql(Object parameterObject) {
        BoundSql boundSql = super.getBoundSql(parameterObject);
        if(isNotNull(conditionSql)){
            String sql = boundSql.getSql();//old sql
            String newSql = null;
            try {
                newSql = SqlUtil.addConditionToSql(sql, conditionSql);
                newSql = replaceLimit(newSql);
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
