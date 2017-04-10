package hap.extend.core.dataPermission.utils;

import com.github.pagehelper.parser.Parser;
import com.github.pagehelper.sqlsource.OrderByStaticSqlSource;
import com.github.pagehelper.sqlsource.PageDynamicSqlSource;
import net.sf.jsqlparser.JSQLParserException;
import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.scripting.xmltags.*;
import org.apache.ibatis.session.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;

import static hap.extend.core.dataPermission.utils.LangUtils.isNotNull;
import static hap.extend.core.dataPermission.utils.SqlUtil.replaceLimit;

/**
 * Created by yyz on 17/4/6.
 */
public class NewDPDynamicSqlSource extends PageDynamicSqlSource {
    private Configuration configuration;
    private SqlNode rootSqlNode;
    private ThreadLocal<String> tlOfConditionSql = new ThreadLocal<>();
    private Logger logger = LoggerFactory.getLogger(NewDPDynamicSqlSource.class);

    public NewDPDynamicSqlSource(DynamicSqlSource sqlSource) {
        super(sqlSource);
    }
    public NewDPDynamicSqlSource(Configuration configuration, SqlNode rootSqlNode, String conditionSql) {
        this(new DynamicSqlSource(configuration, rootSqlNode));
        this.configuration = configuration;
        this.rootSqlNode = rootSqlNode;
        this.tlOfConditionSql.set(conditionSql);
//        BoundSql boundSql = super.getBoundSql(parameterObject);
//        String sql = boundSql.getSql();//old sql
//        if(isNotNull(threadLocal) && isNotNull(threadLocal.get())){
//            String newSql = null;
//            try {
//                newSql = SqlUtil.addConditionToSql(sql, threadLocal.get(),false);
//                newSql = replaceLimit(newSql);
//                MetaObject metaObject = SystemMetaObject.forObject(boundSql);
//                metaObject.setValue("sql", newSql);
//                SqlNode sqlNode = new StaticTextSqlNode(newSql);
//                this.rootSqlNode = sqlNode;
//            } catch (JSQLParserException e) {
//                e.printStackTrace();
//                logger.error(e.getMessage(),e);
//            }
//        }
    }


    protected BoundSql getDefaultBoundSql(Object parameterObject) {
        DynamicContext context = new DynamicContext(this.configuration, parameterObject);
        this.rootSqlNode.apply(context);
        SqlSourceBuilder sqlSourceParser = new SqlSourceBuilder(this.configuration);
        Class parameterType = parameterObject == null?Object.class:parameterObject.getClass();
        SqlSource sqlSource = sqlSourceParser.parse(context.getSql(), parameterType, context.getBindings());

        BoundSql boundSql_0 = sqlSource.getBoundSql(parameterObject);
        String newSql = boundSql_0.getSql();
        if(isNotNull(tlOfConditionSql) && isNotNull(tlOfConditionSql.get())){
            try {
                newSql = SqlUtil.addConditionToSql_0(newSql, tlOfConditionSql.get());
                newSql = removeLimitOffset(newSql);
                MetaObject metaObject = SystemMetaObject.forObject(boundSql_0);
                metaObject.setValue("sql", newSql);
                sqlSource = sqlSourceParser.parse(newSql, parameterType, context.getBindings());
            } catch (JSQLParserException e) {
                logger.error(e.getMessage(),e);
            }
        }

        OrderByStaticSqlSource sqlSource1 = new OrderByStaticSqlSource((StaticSqlSource)sqlSource);
        BoundSql boundSql = sqlSource1.getBoundSql(parameterObject);
        Iterator var7 = context.getBindings().entrySet().iterator();

        while(var7.hasNext()) {
            Map.Entry entry = (Map.Entry)var7.next();
            boundSql.setAdditionalParameter((String)entry.getKey(), entry.getValue());
        }

        return boundSql;
    }
    protected BoundSql getCountBoundSql(Object parameterObject) {
        DynamicContext context = new DynamicContext(this.configuration, parameterObject);
        this.rootSqlNode.apply(context);
        SqlSourceBuilder sqlSourceParser = new SqlSourceBuilder(this.configuration);
        Class parameterType = parameterObject == null?Object.class:parameterObject.getClass();
        SqlSource sqlSource = sqlSourceParser.parse(context.getSql(), parameterType, context.getBindings());
        BoundSql boundSql = sqlSource.getBoundSql(parameterObject);

        StaticSqlSource sqlSource1 = null;
        String newSql = boundSql.getSql();
        boolean flag = false;
        if(isNotNull(tlOfConditionSql) && isNotNull(tlOfConditionSql.get())){
            try {
                newSql = SqlUtil.addConditionToSql_0(newSql, tlOfConditionSql.get());
                newSql = removeLimitOffset(newSql);
                MetaObject metaObject = SystemMetaObject.forObject(boundSql);
                metaObject.setValue("sql", newSql);
                sqlSource1 = new StaticSqlSource(this.configuration, ((Parser)localParser.get()).getCountSql(newSql), boundSql.getParameterMappings());
                flag = true;
            } catch (JSQLParserException e) {
                logger.error(e.getMessage(),e);
            }
        }
        if(!flag){
            sqlSource1 = new StaticSqlSource(this.configuration, ((Parser)localParser.get()).getCountSql(boundSql.getSql()), boundSql.getParameterMappings());
        }


        boundSql = sqlSource1.getBoundSql(parameterObject);
        Iterator var7 = context.getBindings().entrySet().iterator();

        while(var7.hasNext()) {
            Map.Entry entry = (Map.Entry)var7.next();
            boundSql.setAdditionalParameter((String)entry.getKey(), entry.getValue());
        }

        return boundSql;
    }


    protected BoundSql getPageBoundSql(Object parameterObject) {
        DynamicContext context;
        if(parameterObject != null && parameterObject instanceof Map && ((Map)parameterObject).containsKey("_ORIGINAL_PARAMETER_OBJECT")) {
            context = new DynamicContext(this.configuration, ((Map)parameterObject).get("_ORIGINAL_PARAMETER_OBJECT"));
        } else {
            context = new DynamicContext(this.configuration, parameterObject);
        }

        this.rootSqlNode.apply(context);
        SqlSourceBuilder sqlSourceParser = new SqlSourceBuilder(this.configuration);
        Class parameterType = parameterObject == null?Object.class:parameterObject.getClass();
        SqlSource sqlSource = sqlSourceParser.parse(context.getSql(), parameterType, context.getBindings());
        OrderByStaticSqlSource sqlSource1 = new OrderByStaticSqlSource((StaticSqlSource)sqlSource);
        BoundSql boundSql = sqlSource1.getBoundSql(parameterObject);
//        StaticSqlSource sqlSource2 = new StaticSqlSource(this.configuration, ((Parser)localParser.get()).getPageSql(boundSql.getSql()), ((Parser)localParser.get()).getPageParameterMapping(this.configuration, boundSql));
        StaticSqlSource sqlSource2 = null;
        String newSql = boundSql.getSql();
        boolean flag = false;
        if(isNotNull(tlOfConditionSql) && isNotNull(tlOfConditionSql.get())){
            try {
                newSql = SqlUtil.addConditionToSql_0(newSql, tlOfConditionSql.get());
                newSql = removeLimitOffset(newSql);
                MetaObject metaObject = SystemMetaObject.forObject(boundSql);
                metaObject.setValue("sql", newSql);
                sqlSource2 = new StaticSqlSource(this.configuration, ((Parser)localParser.get()).getPageSql(newSql), ((Parser)localParser.get()).getPageParameterMapping(this.configuration, boundSql));
                flag = true;
            } catch (JSQLParserException e) {
                logger.error(e.getMessage(),e);
            }
        }
        if(!flag){
            sqlSource2 = new StaticSqlSource(this.configuration, ((Parser)localParser.get()).getPageSql(boundSql.getSql()), ((Parser)localParser.get()).getPageParameterMapping(this.configuration, boundSql));
        }

        boundSql = sqlSource2.getBoundSql(parameterObject);
        Iterator var7 = context.getBindings().entrySet().iterator();

        while(var7.hasNext()) {
            Map.Entry entry = (Map.Entry)var7.next();
            boundSql.setAdditionalParameter((String)entry.getKey(), entry.getValue());
        }

        return boundSql;
    }

    //多余
//    @Override
//    public BoundSql getBoundSql(Object parameterObject) {
//        BoundSql boundSql = super.getBoundSql(parameterObject);
//        String sql = boundSql.getSql();//old sql
//        if(isNotNull(tlOfConditionSql) && isNotNull(tlOfConditionSql.get())){
//            String newSql = null;
//            try {
//                newSql = SqlUtil.addConditionToSql_0(sql, tlOfConditionSql.get());
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
//        return boundSql;
//    }

    private String removeLimitOffset(String sourceSql){
        return sourceSql.replaceAll("LIMIT \\? OFFSET \\?","");
    }




//    protected BoundSql getDefaultBoundSql(Object parameterObject) {
//        DynamicContext context = new DynamicContext(this.configuration, parameterObject);
//        this.rootSqlNode.apply(context);
//        SqlSourceBuilder sqlSourceParser = new SqlSourceBuilder(this.configuration);
//        Class parameterType = parameterObject == null?Object.class:parameterObject.getClass();
//        SqlSource sqlSource = sqlSourceParser.parse(context.getSql(), parameterType, context.getBindings());
//        OrderByStaticSqlSource sqlSource1 = new OrderByStaticSqlSource((StaticSqlSource)sqlSource);
//        BoundSql boundSql = sqlSource1.getBoundSql(parameterObject);
//        Iterator var7 = context.getBindings().entrySet().iterator();
//
//        while(var7.hasNext()) {
//            Map.Entry entry = (Map.Entry)var7.next();
//            boundSql.setAdditionalParameter((String)entry.getKey(), entry.getValue());
//        }
//
////just new sql source
//        String newSql = boundSql.getSql();
//        if(isNotNull(tlOfConditionSql) && isNotNull(tlOfConditionSql.get())){
//            try {
//                newSql = SqlUtil.addConditionToSql_0(newSql, tlOfConditionSql.get());
//                newSql = removeLimitOffset(newSql);
//                MetaObject metaObject = SystemMetaObject.forObject(boundSql);
//                metaObject.setValue("sql", newSql);
//            } catch (JSQLParserException e) {
//                logger.error(e.getMessage(),e);
//            }
//        }
//        return boundSql;
//    }
//
//    protected BoundSql getCountBoundSql(Object parameterObject) {
//        DynamicContext context = new DynamicContext(this.configuration, parameterObject);
//        this.rootSqlNode.apply(context);
//        SqlSourceBuilder sqlSourceParser = new SqlSourceBuilder(this.configuration);
//        Class parameterType = parameterObject == null?Object.class:parameterObject.getClass();
//
//        SqlSource sqlSource = sqlSourceParser.parse(context.getSql(), parameterType, context.getBindings());
//        BoundSql boundSql = sqlSource.getBoundSql(parameterObject);
//        String newSql = boundSql.getSql();
//        if(isNotNull(tlOfConditionSql) && isNotNull(tlOfConditionSql.get())){
//            try {
//                newSql = SqlUtil.addConditionToSql_0(newSql, tlOfConditionSql.get());
//                newSql = removeLimitOffset(newSql);
//                MetaObject metaObject = SystemMetaObject.forObject(boundSql);
//                metaObject.setValue("sql", newSql);
//            } catch (JSQLParserException e) {
//                logger.error(e.getMessage(),e);
//            }
//        }
//        String temp = boundSql.getSql();
//        StaticSqlSource sqlSource1 = new StaticSqlSource(this.configuration, ((Parser)localParser.get()).getCountSql(boundSql.getSql()), boundSql.getParameterMappings());
//        boundSql = sqlSource1.getBoundSql(parameterObject);
//        Iterator var7 = context.getBindings().entrySet().iterator();
//
//        while(var7.hasNext()) {
//            Map.Entry entry = (Map.Entry)var7.next();
//            boundSql.setAdditionalParameter((String)entry.getKey(), entry.getValue());
//        }
//        return boundSql;
//    }
//
//    protected BoundSql getPageBoundSql(Object parameterObject) {
//        DynamicContext context;
//        if(parameterObject != null && parameterObject instanceof Map && ((Map)parameterObject).containsKey("_ORIGINAL_PARAMETER_OBJECT")) {
//            context = new DynamicContext(this.configuration, ((Map)parameterObject).get("_ORIGINAL_PARAMETER_OBJECT"));
//        } else {
//            context = new DynamicContext(this.configuration, parameterObject);
//        }
//
//        this.rootSqlNode.apply(context);
//        SqlSourceBuilder sqlSourceParser = new SqlSourceBuilder(this.configuration);
//        Class parameterType = parameterObject == null?Object.class:parameterObject.getClass();
//
//
//        SqlSource sqlSource = sqlSourceParser.parse(context.getSql(), parameterType, context.getBindings());
//        OrderByStaticSqlSource sqlSource1 = new OrderByStaticSqlSource((StaticSqlSource)sqlSource);
//        BoundSql boundSql = sqlSource1.getBoundSql(parameterObject);
//        String newSql = boundSql.getSql();
//        if(isNotNull(tlOfConditionSql) && isNotNull(tlOfConditionSql.get())){
//            try {
//                newSql = SqlUtil.addConditionToSql_0(newSql, tlOfConditionSql.get());
//                newSql = removeLimitOffset(newSql);
//                MetaObject metaObject = SystemMetaObject.forObject(boundSql);
//                metaObject.setValue("sql", newSql);
//            } catch (JSQLParserException e) {
//                logger.error(e.getMessage(),e);
//            }
//        }
//        String temp = boundSql.getSql();
//        StaticSqlSource sqlSource2 = new StaticSqlSource(this.configuration, ((Parser)localParser.get()).getPageSql(boundSql.getSql()), ((Parser)localParser.get()).getPageParameterMapping(this.configuration, boundSql));
//        boundSql = sqlSource2.getBoundSql(parameterObject);
//        Iterator var7 = context.getBindings().entrySet().iterator();
//
//        while(var7.hasNext()) {
//            Map.Entry entry = (Map.Entry)var7.next();
//            boundSql.setAdditionalParameter((String)entry.getKey(), entry.getValue());
//        }
//
//        return boundSql;
//    }


    public Configuration getConfiguration() {
        return configuration;
    }

    public SqlNode getRootSqlNode() {
        return rootSqlNode;
    }

    public ThreadLocal<String> getTlOfConditionSql() {
        return tlOfConditionSql;
    }
}
