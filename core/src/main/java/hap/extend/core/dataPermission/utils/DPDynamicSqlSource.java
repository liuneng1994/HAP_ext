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
    private ThreadLocal<String> tlOfConditionSql;
    private ThreadLocal<Boolean> tlOfIsCountFlag;

    public DPDynamicSqlSource(Configuration configuration, SqlNode rootSqlNode, Object parameterObj, ThreadLocal<String> threadLocal, ThreadLocal<Boolean> tlOfIsCountFlag) {
        super(configuration, rootSqlNode);
        this.configuration = configuration;
        this.rootSqlNode = rootSqlNode;
        this.tlOfConditionSql = threadLocal;
        this.tlOfIsCountFlag = tlOfIsCountFlag;
//        DynamicContext context = new DynamicContext(this.configuration, parameterObj);
//        context.appendSql("<where> "+ threadLocal.get()+" </where>");//TODO 关键 ===================================
//        this.rootSqlNode.apply(context);
//        BoundSql boundSql = super.getBoundSql(parameterObj);
//        String sql = boundSql.getSql();//old sql
//        if(isNotNull(threadLocal) && isNotNull(threadLocal.get())){
//            String newSql = null;
//            try {
//                newSql = SqlUtil.addConditionToSql(sql, threadLocal.get(),tlOfIsCountFlag.get());
//                newSql = replaceLimit(newSql);
//            } catch (JSQLParserException e) {
//                e.printStackTrace();
//                logger.error(e.getMessage(),e);
//            }
//            MetaObject metaObject = SystemMetaObject.forObject(boundSql);
//            metaObject.setValue("sql", newSql);
//        }
    }

    @Override
    public BoundSql getBoundSql(Object parameterObject) {
        BoundSql boundSql = super.getBoundSql(parameterObject);
        String sql = boundSql.getSql();//old sql
        if(isNotNull(tlOfConditionSql) && isNotNull(tlOfConditionSql.get())){
            String newSql = null;
            try {
                newSql = SqlUtil.addConditionToSql(sql, tlOfConditionSql.get(),tlOfIsCountFlag.get());
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
