package hap.extend.core.dataPermission.interceptor;

import hap.extend.core.dataPermission.utils.*;
import com.hand.hap.cache.Cache;
import com.hand.hap.cache.CacheManager;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.impl.RequestHelper;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import java.util.*;

import static hap.extend.core.dataPermission.utils.LangUtils.isNotNull;

/**
 * Created by yyz on 2017/2/14.
 *
 * @author yazheng.yang@hand-china.com
 */
@Intercepts(@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}))//第二个参数是封装了sql的参数对象
public class DataPermissionInterceptor implements Interceptor {
    private static Logger logger = LoggerFactory.getLogger(DataPermissionInterceptor.class);

    private static final String FIELD_USER_ID = "#userId#";
    private static final String FIELD_ROLE_ID = "#roleId#";
    private static final String FIELD_SQL_AND = "AND";
    private static final String FIELD_SQL_OR = "OR";

    @Autowired
    private ApplicationContext applicationContext;

    private Cache<Long[]> ruleIdsOfMethodMappingCache;
    private Cache<String> ruleUserMappingCache;
    private Cache<String> rulesCache;

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
        String sqlId = statement.getId();
        sqlId = extractMapperMethod(sqlId);
        initCacheObj();
        if(isNull(ruleIdsOfMethodMappingCache) || isNull(ruleUserMappingCache) || isNull(rulesCache)){
            logger.error("\nHAP通用数据权限：权限设置没有加入缓存\n");
            logger.error("\n+++++++++++++++++++++++++++++++++++++++\n");
            logger.error("缓存："+Constant.FIELD_RULES_CACHE_NAME+",added:"+isNull(rulesCache));
            logger.error("缓存："+Constant.FIELD_RULE_USER_CACHE_NAME+",added:"+!isNull(ruleUserMappingCache));
            logger.error("缓存："+Constant.FIELD_METHOD_RULES_MAP_CACHE_NAME+",added:"+!isNull(ruleIdsOfMethodMappingCache));
            logger.error("\n+++++++++++++++++++++++++++++++++++++++\n");
            return jumpIntercept(invocation);
        }
        Long[] ruleIds = ruleIdsOfMethodMappingCache.getValue(CacheUtils.getMappermethodRulesKey(sqlId));
        if(isNull(ruleIds)){
            return jumpIntercept(invocation);
        }

        //fetch final rule ids to applied upon this method ,this user
        Set<String> filteredRuleKeys = new HashSet<>();
        for (Long ruleId : ruleIds){
            //is include type
            String excludeRule = rulesCache.getValue(CacheUtils.getRuleKey(ruleId.toString(), false));
            if(isNotNull(excludeRule)){
                String excludeValue = ruleUserMappingCache.getValue(CacheUtils.getRuleUserKey(ruleId.toString(), userId_L.toString(), false));
                if(isNotNull(excludeValue)){
                    continue;
                }else {
                    filteredRuleKeys.add(CacheUtils.getRuleKey(ruleId.toString(), false));
                }
            } else{
                String includeValue = ruleUserMappingCache.getValue(CacheUtils.getRuleUserKey(ruleId.toString(),userId_L.toString(),true));
                if(isNotNull(includeValue)){
                    filteredRuleKeys.add(CacheUtils.getRuleKey(ruleId.toString(), true));
                }
            }
        }
        if(filteredRuleKeys.isEmpty()){
            return jumpIntercept(invocation);
        }
        String conditionSql = handleRuleList(userId_L.toString(), roleId_L.toString(), filteredRuleKeys, rulesCache);
        if(isNull(conditionSql)){
            return jumpIntercept(invocation);
        }
//        //apply rules below
//        BoundSql boundSql = statement.getBoundSql(args[1]);
//        String oldSql = boundSql.getSql();
//        Assert.notNull(oldSql,"需要执行的sql不能为null");
//        Assert.isTrue(oldSql.length() > 0,"需要执行的sql不能为空串");
        SqlSource sqlSource = statement.getSqlSource();
        SqlSource newSqlSource1 = SqlSourceUtil.covertSqlSource(sqlSource, conditionSql);
        MetaObject msObject = SystemMetaObject.forObject(statement);
        msObject.setValue("sqlSource", newSqlSource1);

        return invocation.proceed();
    }

    private Object jumpIntercept(Invocation invocation) throws Throwable {
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties properties) {

    }

    private void initCacheObj(){
        //cache:rule ids applied upon this method
        if(isNull(ruleIdsOfMethodMappingCache)){
            ruleIdsOfMethodMappingCache = applicationContext.getBean(CacheManager.class).getCache(Constant.FIELD_METHOD_RULES_MAP_CACHE_NAME);
        }
        //cache:rule、user,whether this user is to apply rule
        if(isNull(ruleUserMappingCache)){
            ruleUserMappingCache = applicationContext.getBean(CacheManager.class).getCache(Constant.FIELD_RULE_USER_CACHE_NAME);
        }
        //cache:rules
        if(isNull(rulesCache)){
            rulesCache = applicationContext.getBean(CacheManager.class).getCache(Constant.FIELD_RULES_CACHE_NAME);
        }
    }


    private final boolean isNull(Object object){
        return null == object;
    }

    private String handleRuleList(final String userId, final String roleId, Set<String> ruleKeys,Cache<String> allRulesInCache){
        if(null == ruleKeys || ruleKeys.size() < 1){
            return null;
        }
        if(isNull(allRulesInCache)){
            return null;
        }
        Set<String> rulesSet = new HashSet<>();
        //fetch rules sql list
        for(String key : ruleKeys){
            if(!isNull(key)){
                String tempSql = allRulesInCache.getValue(key);
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

    /**
     * 判断是否是用于分页的mapper method全路径（会在方法的后面加“_COUNT”）
     * @param mapperMethodId
     * @return
     */
    private boolean isPrePagingMethod(String mapperMethodId){
        if(isNull(mapperMethodId)){
            return false;
        }
        return mapperMethodId.endsWith("_COUNT");
    }

    private String extractMapperMethod(String mapperMethodId){
        if(isPrePagingMethod(mapperMethodId)){
            return mapperMethodId.substring(0,mapperMethodId.length()-6);
        }
        return mapperMethodId;
    }
}
