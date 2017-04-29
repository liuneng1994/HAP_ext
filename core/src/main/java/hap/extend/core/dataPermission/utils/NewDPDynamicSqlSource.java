package hap.extend.core.dataPermission.utils;

import com.github.pagehelper.parser.Parser;
import com.github.pagehelper.sqlsource.OrderByStaticSqlSource;
import com.github.pagehelper.sqlsource.PageDynamicSqlSource;
import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.xmltags.*;
import org.apache.ibatis.session.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by yyz on 17/4/6.
 */
public class NewDPDynamicSqlSource extends PageDynamicSqlSource {
    private Configuration configuration;
    private SqlNode rootSqlNode;
    private Logger logger = LoggerFactory.getLogger(NewDPDynamicSqlSource.class);

    public NewDPDynamicSqlSource(DynamicSqlSource sqlSource) {
        super(sqlSource);
    }

    public NewDPDynamicSqlSource(Configuration configuration, SqlNode rootSqlNode) {
        this(new DynamicSqlSource(configuration, rootSqlNode));
        this.configuration = configuration;
        this.rootSqlNode = rootSqlNode;
    }

    protected BoundSql getDefaultBoundSql(Object parameterObject) {
        DynamicContext context = new DynamicContext(this.configuration, parameterObject);
        this.rootSqlNode.apply(context);
        DPSqlSourceBuilder sqlSourceParser = new DPSqlSourceBuilder(this.configuration);
        Class parameterType = parameterObject == null?Object.class:parameterObject.getClass();
        SqlSource sqlSource = sqlSourceParser.parse(context.getSql(), parameterType, context.getBindings());
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
        DPSqlSourceBuilder sqlSourceParser = new DPSqlSourceBuilder(this.configuration);
        Class parameterType = parameterObject == null?Object.class:parameterObject.getClass();
        SqlSource sqlSource = sqlSourceParser.parse(context.getSql(), parameterType, context.getBindings());
        BoundSql boundSql = sqlSource.getBoundSql(parameterObject);

        StaticSqlSource sqlSource1 = new StaticSqlSource(this.configuration, ((Parser)localParser.get()).getCountSql(boundSql.getSql()), boundSql.getParameterMappings());

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
        DPSqlSourceBuilder sqlSourceParser = new DPSqlSourceBuilder(this.configuration);
        Class parameterType = parameterObject == null?Object.class:parameterObject.getClass();
        SqlSource sqlSource = sqlSourceParser.parse(context.getSql(), parameterType, context.getBindings());
        OrderByStaticSqlSource sqlSource1 = new OrderByStaticSqlSource((StaticSqlSource)sqlSource);
        BoundSql boundSql = sqlSource1.getBoundSql(parameterObject);
        StaticSqlSource sqlSource2 = new StaticSqlSource(this.configuration, ((Parser)localParser.get()).getPageSql(boundSql.getSql()), ((Parser)localParser.get()).getPageParameterMapping(this.configuration, boundSql));
        boundSql = sqlSource2.getBoundSql(parameterObject);
        Iterator var7 = context.getBindings().entrySet().iterator();

        while(var7.hasNext()) {
            Map.Entry entry = (Map.Entry)var7.next();
            boundSql.setAdditionalParameter((String)entry.getKey(), entry.getValue());
        }

        return boundSql;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public SqlNode getRootSqlNode() {
        return rootSqlNode;
    }
}
