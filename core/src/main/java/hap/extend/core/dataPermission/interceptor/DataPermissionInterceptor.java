package hap.extend.core.dataPermission.interceptor;

import hap.extend.core.dataPermission.utils.CacheUtils;
import hap.extend.core.dataPermission.utils.Constant;
import com.hand.hap.cache.Cache;
import com.hand.hap.cache.CacheManager;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.impl.RequestHelper;
import hap.extend.core.dataPermission.utils.LangUtils;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import org.apache.ibatis.executor.Executor;
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
import java.util.*;

/**
 * Created by yyz on 2017/2/14.
 *
 * @author yazheng.yang@hand-china.com
 */

//@Intercepts({
//        @Signature(type = Executor.class, method = "createCacheKey", args = { MappedStatement.class, Object.class,
//                RowBounds.class, BoundSql.class }),
//        @Signature(type = StatementHandler.class, method = "parameterize", args = { Statement.class }) })
@Intercepts(@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}))//第二个参数是封装了sql的参数对象
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
    private static final String FIELD_SQL_OR = "OR";
    private static final String FIELD_SQL_WHERE = "WHERE";

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        IRequest request = RequestHelper.getCurrentRequest(true);
        Long userId_L = request.getUserId();
        Long roleId_L = request.getRoleId();
        if(isNull(userId_L) || userId_L < 0){
            return jumpIntercept(invocation);
        }

        final Object[] args = invocation.getArgs();
        MappedStatement statement = (MappedStatement) args[0];//在当前类的开头使用注解规定需要注入的参数
//        logger.debug("\n\n\n拦截的sql id------------------------"+statement.getId());

        //cache:rule ids applied upon this method
        Cache<Long[]> ruleIdsOfMethodMappingCache = applicationContext.getBean(CacheManager.class).getCache(Constant.FIELD_METHOD_RULES_MAP_CACHE_NAME);
        //cache:rule、user,whether this user is to apply rule
        Cache<String> ruleUserMappingCache = applicationContext.getBean(CacheManager.class).getCache(Constant.FIELD_RULE_USER_CACHE_NAME);
        //cache:rules
        Cache<String> rulesCache = applicationContext.getBean(CacheManager.class).getCache(Constant.FIELD_RULES_CACHE_NAME);
        if(isNull(ruleIdsOfMethodMappingCache) || isNull(ruleUserMappingCache) || isNull(rulesCache)){
            logger.error("\nHAP通用数据权限：权限设置没有加入缓存\n");
            logger.error("\n+++++++++++++++++++++++++++++++++++++++\n");
            logger.error("缓存："+Constant.FIELD_RULES_CACHE_NAME+",added:"+isNull(rulesCache));
            logger.error("缓存："+Constant.FIELD_RULE_USER_CACHE_NAME+",added:"+!isNull(ruleUserMappingCache));
            logger.error("缓存："+Constant.FIELD_METHOD_RULES_MAP_CACHE_NAME+",added:"+!isNull(ruleIdsOfMethodMappingCache));
            logger.error("\n+++++++++++++++++++++++++++++++++++++++\n");
            return jumpIntercept(invocation);
        }
        Long[] ruleIds = ruleIdsOfMethodMappingCache.getValue(CacheUtils.getMappermethodRulesKey(statement.getId()));
        if(isNull(ruleIds)){
            return jumpIntercept(invocation);
        }

        //fetch final rule ids to applied upon this method ,this user
        Set<Long> filteredRuleIds = new HashSet<>();
        for (Long ruleId : ruleIds){
            String excludeValue = ruleUserMappingCache.getValue(CacheUtils.getRuleUserKey(ruleId.toString(), userId_L.toString(), false));
            if(LangUtils.isNotNull(excludeValue)){
                continue;
            }
            String includeValue = ruleUserMappingCache.getValue(CacheUtils.getRuleUserKey(ruleId.toString(),userId_L.toString(),true));
            if(LangUtils.isNotNull(includeValue)){
                filteredRuleIds.add(ruleId);
            }
        }
        if(filteredRuleIds.isEmpty()){
            return jumpIntercept(invocation);
        }
        String conditionSql = handleRuleList(userId_L.toString(), roleId_L.toString(), filteredRuleIds, rulesCache);
        if(isNull(conditionSql)){
            return jumpIntercept(invocation);
        }
        //apply rules below
        BoundSql boundSql = statement.getBoundSql(args[1]);
        String oldSql = boundSql.getSql();
        Assert.notNull(oldSql,"需要执行的sql不能为null");
        Assert.isTrue(oldSql.length() > 0,"需要执行的sql不能为空串");

        //extract where fragment of old sql
        Select select = (Select) CCJSqlParserUtil.parse(oldSql);
        Expression where = ((PlainSelect) select.getSelectBody()).getWhere();
        Expression expression = null;
        //append condition in where fragment
        try{
            if(isNull(where) || where.toString().length() < 1){
                expression = CCJSqlParserUtil.parseCondExpression(conditionSql);
            }else {
                expression = CCJSqlParserUtil.parseCondExpression(where.toString() + " " + FIELD_SQL_AND + " " + conditionSql);
            }
            ((PlainSelect) select.getSelectBody()).setWhere(expression);
        }catch (Exception e){
            logger.error("data Permission：权限规则不合法(SQL片段:{}),\ndetail:{}",conditionSql,e.getMessage());
            throw new Exception("data Permission：权限规则(SQL片段)不合法:"+conditionSql+"\ndetail:"+e.getMessage());
        }
        logger.info("\n\nsql with data permission:\n{}\n\n",select.toString());
        writeDeclaredField(boundSql, FIELD_SQL, select.toString());

        return invocation.proceed();
    }

    private Object jumpIntercept(Invocation invocation) throws Throwable {
        return invocation.proceed();
    }
//    @Override
//    public Object intercept(Invocation invocation) throws Throwable {
//        IRequest request = RequestHelper.getCurrentRequest(true);
//        Long userId_L = request.getUserId();
//        Long roleId_L = request.getRoleId();
//        if(isNull(userId_L) || userId_L < 0){
//            return invocation.proceed();
//        }
//
//        final Object[] args = invocation.getArgs();
//        MappedStatement statement = (MappedStatement) args[0];
//        logger.debug("\n\n\n拦截的sql id------------------------"+statement.getId());
//
//        BoundSql boundSql = statement.getBoundSql(args[1]);
//        String oldSql = boundSql.getSql();
//        Assert.notNull(oldSql,"需要执行的sql不能为null");
//        Assert.isTrue(oldSql.length() > 0,"需要执行的sql不能为空串");
//
//
////        Assert.notNull(request);
////
////        String userId = request.getUserId().toString();
////        String roleId = request.getRoleId().toString();
//
//        //提取原先sql的where
//        Select select = (Select) CCJSqlParserUtil.parse(oldSql);
//        logger.debug("拦截的sql------------------------\r\n"+oldSql);
//        Expression where = ((PlainSelect) select.getSelectBody()).getWhere();
//        Expression expression = null;
//        //将需要的sql拼接上
//        if(isNull(where) || where.toString().length() < 1){
//            expression = CCJSqlParserUtil.parseCondExpression("1=1");
//        }else {
//            expression = CCJSqlParserUtil.parseCondExpression(where.toString() + " " + FIELD_SQL_AND + " 1=1");
//        }
//        ((PlainSelect) select.getSelectBody()).setWhere(expression);
//        logger.debug("改造后的sql------------------------\r\n"+select.toString());
//        writeDeclaredField(boundSql, FIELD_SQL, select.toString());
//        return invocation.proceed();
//    }

    @Override
    public Object plugin(Object target) {
//        logger.debug("\n\n\n进入plugin method------------------------");
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

    private String handleRuleList(final String userId, final String roleId, Set<Long> ruleIds,Cache<String> allRulesInCache){
        if(null == ruleIds || ruleIds.size() < 1){
            return null;
        }
        if(isNull(allRulesInCache)){
            return null;
        }
        Set<String> rulesSet = new HashSet<>();
        //fetch rules sql list
        for(Long ruleId : ruleIds){
            if(!isNull(ruleId)){
                String tempSql = allRulesInCache.getValue(CacheUtils.getRuleKey(ruleId.toString()));
                if(tempSql.contains(FIELD_USER_ID) || tempSql.contains(FIELD_ROLE_ID)){
                    rulesSet.add(tempSql.replace(FIELD_USER_ID,userId).replace(FIELD_ROLE_ID,roleId));
                }else {
                    rulesSet.add(tempSql);
                }
            }
        }

        StringBuilder stringBuilder = new StringBuilder("(");
        boolean isFirst = true;
        if(rulesSet.isEmpty()){
            return null;
        }
        for(String rule : rulesSet){
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
