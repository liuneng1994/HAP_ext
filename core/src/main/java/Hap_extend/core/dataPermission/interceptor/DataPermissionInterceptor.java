package Hap_extend.core.dataPermission.interceptor;

import Hap_extend.core.dataPermission.utils.CacheUtils;
import Hap_extend.core.dataPermission.utils.Constant;
import com.hand.hap.cache.Cache;
import com.hand.hap.cache.CacheManager;
import com.hand.hap.core.IRequest;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * Created by yyz on 2017/2/14.
 *
 * @author yazheng.yang@hand-china.com
 */

//@Intercepts({
//        @Signature(type = Executor.class, method = "createCacheKey", args = { MappedStatement.class, Object.class,
//                RowBounds.class, BoundSql.class }),
//        @Signature(type = StatementHandler.class, method = "parameterize", args = { Statement.class }) })
@Intercepts(@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}))
public class DataPermissionInterceptor implements Interceptor {
    private static Logger logger = LoggerFactory.getLogger(DataPermissionInterceptor.class);

    private static final String FIELD_DELEGATE = "delegate";
    private static final String FIELD_ROWBOUNDS = "rowBounds";
    private static final String FIELD_MAPPEDSTATEMENT = "mappedStatement";
    private static final String FIELD_SQL = "sql";
    private static final String FIELD_IREQUEST = "request";

    private static final String FIELD_USER_ID = "#userId#";
    private static final String FIELD_ROLE_ID = "#roleId#";
    private static final String FIELD_SQL_AND = "AND";
    private static final String FIELD_SQL_WHERE = "WHERE";

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        final Object[] args = invocation.getArgs();
        MappedStatement statement = (MappedStatement) args[0];
        logger.debug("\n\n\n拦截的sql id------------------------"+statement.getId());

        Cache<Long[]> ruleIdsOfMethodUserMapping = applicationContext.getBean(CacheManager.class).getCache(Constant.FIELD_METHOD_USER_MAP_CACHE_NAME);
        Cache<String> rules = applicationContext.getBean(CacheManager.class).getCache(Constant.FIELD_RULES_CACHE_NAME);
        if(isNull(ruleIdsOfMethodUserMapping) || isNull(rules)){
            logger.error("HAP通用数据权限：权限设置没有加入缓存，"+Constant.FIELD_METHOD_USER_MAP_CACHE_NAME+":"
                    +isNull(ruleIdsOfMethodUserMapping)+","+Constant.FIELD_RULES_CACHE_NAME+":"+isNull(rules));
        }else {//处理规则的应用
            RoutingStatementHandler statementHandler = (RoutingStatementHandler) invocation.getTarget();

            StatementHandler handler = (StatementHandler) readField(statementHandler, FIELD_DELEGATE);
//            MappedStatement mappedStatement = (MappedStatement) readField(handler, FIELD_MAPPEDSTATEMENT);
            BoundSql boundSql = handler.getBoundSql();
            String oldSql = boundSql.getSql();
            Assert.notNull(oldSql,"需要执行的sql不能为null");
            Assert.isTrue(oldSql.length() > 0,"需要执行的sql不能为空串");

            IRequest request = (IRequest) boundSql.getAdditionalParameter(FIELD_IREQUEST);
            Assert.notNull(request);

            String userId = request.getUserId().toString();
            String roleId = request.getRoleId().toString();


            Long[] targetRuleIds = ruleIdsOfMethodUserMapping.getValue(CacheUtils.getRuleIdsOfUserMethodMap(statement.getId(), userId));
            if(isNull(targetRuleIds) || targetRuleIds.length < 1){//无需应用规则
                return invocation.proceed();
            }

            String sqlToAppend = handleRuleList(userId, roleId, targetRuleIds, rules);//规则的sql

            //提取原先sql的where
            Select select = (Select) CCJSqlParserUtil.parse(oldSql);
            Expression where = ((PlainSelect) select.getSelectBody()).getWhere();
            Expression expression = null;
            //将需要的sql拼接上
            if(isNull(where) || where.toString().length() < 1){
                expression = CCJSqlParserUtil.parseCondExpression(sqlToAppend);
            }else {
                expression = CCJSqlParserUtil.parseCondExpression(where.toString() + " " + FIELD_SQL_AND + " " + sqlToAppend);
            }
            ((PlainSelect) select.getSelectBody()).setWhere(expression);

            writeDeclaredField(boundSql, FIELD_SQL, select.toString());
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        logger.debug("\n\n\n进入plugin method------------------------");
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties properties) {

    }


    private final boolean isNull(Object object){
        return null == object;
    }

    private void writeDeclaredField(Object target, String fieldName, Object value)
            throws IllegalAccessException {
        if (target == null) {
            throw new IllegalArgumentException("target object must not be null");
        }
        Class<?> cls = target.getClass();
        Field field = getField(cls, fieldName);
        if (field == null) {
            throw new IllegalArgumentException("Cannot locate declared field " + cls.getName() + "." + fieldName);
        }
        field.set(target, value);
    }
    private static Field getField(final Class<?> cls, String fieldName) {
        for (Class<?> acls = cls; acls != null; acls = acls.getSuperclass()) {
            try {
                Field field = acls.getDeclaredField(fieldName);
                if (!Modifier.isPublic(field.getModifiers())) {
                    field.setAccessible(true);
                    return field;
                }
            } catch (NoSuchFieldException ex) {
                // ignore
            }
        }
        return null;
    }
    private Object readField(Object target, String fieldName)
            throws IllegalAccessException {
        if (target == null) {
            throw new IllegalArgumentException("target object must not be null");
        }
        Class<?> cls = target.getClass();
        Field field = getField(cls, fieldName);
        if (field == null) {
            throw new IllegalArgumentException("Cannot locate field " + fieldName + " on " + cls);
        }
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
        return field.get(target);
    }

    private String handleRuleList(final String userId, final String roleId, Long[] ruleIds,Cache<String> allRulesInCache){
        Assert.notNull(ruleIds);
        if(ruleIds.length < 1){
            return null;
        }
        if(isNull(allRulesInCache)){
            return null;
        }
        List<String> ruleList = new ArrayList<>();
        //fetch rules sql list
        for(Long ruleId : ruleIds){
            if(!isNull(ruleId)){
                String tempSql = allRulesInCache.getValue(CacheUtils.getRuleId(ruleId.toString()));
                if(tempSql.contains(FIELD_USER_ID) || tempSql.contains(FIELD_ROLE_ID)){
                    ruleList.add(tempSql.replace(FIELD_USER_ID,userId).replace(FIELD_ROLE_ID,roleId));
                }
            }
        }

        StringBuilder stringBuilder = new StringBuilder("(");
        boolean isFirst = true;
        List<String> collect = ruleList.parallelStream().distinct().collect(Collectors.toList());
        if(isNull(collect) || collect.size()<1){
            return null;
        }
        for(String rule : collect){
            if(isFirst){
                stringBuilder.append("("+rule+")");
                isFirst = false;
                continue;
            }
            stringBuilder.append(" "+FIELD_SQL_AND+" ("+rule+")");
        }
        stringBuilder.append(")");
        return stringBuilder.toString();
    }
}
