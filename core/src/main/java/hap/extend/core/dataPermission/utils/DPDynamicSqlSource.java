package hap.extend.core.dataPermission.utils;

import com.hand.hap.core.impl.RequestHelper;
import hap.extend.core.dataPermission.interceptor.DataPermissionInterceptor;
import hap.extend.core.operation.utils.OPConstUtil;
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
import static hap.extend.core.dataPermission.utils.LangUtils.isNull;
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

//    public DPDynamicSqlSource(Configuration configuration, SqlNode rootSqlNode, Object parameterObj, ThreadLocal<String> threadLocal, ThreadLocal<Boolean> tlOfIsCountFlag) {
//        super(configuration, rootSqlNode);
//        this.configuration = configuration;
//        this.rootSqlNode = rootSqlNode;
//        this.tlOfConditionSql = threadLocal;
//        this.tlOfIsCountFlag = tlOfIsCountFlag;
//    }
    public DPDynamicSqlSource(Configuration configuration, SqlNode rootSqlNode, Object parameterObj) {
        super(configuration, rootSqlNode);
        this.configuration = configuration;
        this.rootSqlNode = rootSqlNode;
        this.tlOfIsCountFlag = tlOfIsCountFlag;
    }

    @Override
    public BoundSql getBoundSql(Object parameterObject) {
        BoundSql boundSql = super.getBoundSql(parameterObject);
        String sql = boundSql.getSql();//old sql
        String conditionSql = DataPermissionInterceptor.getConditionSql();
        if(isNotNull(conditionSql)){
            String newSql = null;
            try {
                newSql = SqlUtil.addConditionToSql(sql, conditionSql,tlOfIsCountFlag.get());
                newSql = replaceLimit(newSql);
            } catch (JSQLParserException e) {
                e.printStackTrace();
                logger.error(e.getMessage(),e);
            }
            MetaObject metaObject = SystemMetaObject.forObject(boundSql);
            metaObject.setValue("sql", newSql);
        }

        OPConstUtil.setRequestParameterInBundSql(boundSql);
        return boundSql;
//        BoundSql boundSql = super.getBoundSql(parameterObject);
//        String sql = boundSql.getSql();//old sql
//        if(isNotNull(tlOfConditionSql) && isNotNull(tlOfConditionSql.get())){
//            String newSql = null;
//            try {
//                newSql = SqlUtil.addConditionToSql(sql, tlOfConditionSql.get(),tlOfIsCountFlag.get());
//                newSql = replaceLimit(newSql);
//            } catch (JSQLParserException e) {
//                e.printStackTrace();
//                logger.error(e.getMessage(),e);
//            }
//            MetaObject metaObject = SystemMetaObject.forObject(boundSql);
//            metaObject.setValue("sql", newSql);
//        }else {
//            MetaObject metaObject = SystemMetaObject.forObject(boundSql);
//            metaObject.setValue("sql", sql);
//        }
//        OPConstUtil.setRequestParameterInBundSql(boundSql);
//        return boundSql;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public SqlNode getRootSqlNode() {
        return rootSqlNode;
    }
//
//    public ThreadLocal<String> getTlOfConditionSql() {
//        return tlOfConditionSql;
//    }
//
//    public ThreadLocal<Boolean> getTlOfIsCountFlag() {
//        return tlOfIsCountFlag;
//    }
}
