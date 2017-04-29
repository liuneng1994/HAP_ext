package hap.extend.core.dataPermission.interceptor;

import hap.extend.core.dataPermission.dto.RuleUser;
import hap.extend.core.dataPermission.utils.*;
import com.hand.hap.cache.Cache;
import com.hand.hap.cache.CacheManager;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.impl.RequestHelper;
import hap.extend.core.operation.utils.OPConstUtil;
import org.apache.commons.collections.list.TransformedList;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
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

    private static final ThreadLocal<String> conditionSqlThisThread = new ThreadLocal<>();

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        IRequest request = RequestHelper.getCurrentRequest(true);
        Long userId_L = request.getUserId();
        Long roleId_L = request.getRoleId();
        logger.info("\n(userId,roleId,sqlId)=({},{})\n",userId_L,roleId_L);
        if(isNull(userId_L) || userId_L < 0){
            return jumpIntercept(invocation);
        }
        final Object[] args = invocation.getArgs();
        MappedStatement statement = (MappedStatement) args[0];//在当前类的开头使用注解规定需要注入的参数
        SqlSource sqlSource = statement.getSqlSource();
        BoundSql boundSql = sqlSource.getBoundSql(args[1]);
        Object requestObj = boundSql.getAdditionalParameter(OPConstUtil.HAP_REQUEST_CONTEXT_REQUEST_KEY);
//        IRequest request = null;
//        logger.info("\nhap设置的request1：{}\nhap设置的request2：{}\n\n",requestObj,RequestHelper.getCurrentRequest(true));
//        if(isNull(requestObj)){
//            request = RequestHelper.getCurrentRequest(true);
//        }else {
//            request = (IRequest)requestObj;
//        }
//        Long userId_L = request.getUserId();
//        Long roleId_L = request.getRoleId();
//        if(isNull(userId_L) || userId_L < 0){
//            return jumpIntercept(invocation);
//        }
        String sqlId = statement.getId();
        logger.info("\ndata permission:is going to handle(userId,roleId,sqlId)=({},{},{})\n",userId_L,roleId_L,sqlId);
        boolean isCountFlag = isPrePagingMethod(sqlId);
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
            if(isNotNull(excludeRule)){//rule of exclude type
                //role
                String isThisRoleExcluded = ruleUserMappingCache.getValue(CacheUtils.getRuleUserKey(ruleId.toString(), RuleUser.TYPE_ROLE,roleId_L.toString()));
                if(isNull(isThisRoleExcluded)){
                    filteredRuleKeys.add(CacheUtils.getRuleKey(ruleId.toString(), false));
                    continue;
                }
                //user
                String isThisUserExcluded = ruleUserMappingCache.getValue(CacheUtils.getRuleUserKey(ruleId.toString(), RuleUser.TYPE_USER,userId_L.toString()));
                if(isNull(isThisUserExcluded)){
                    filteredRuleKeys.add(CacheUtils.getRuleKey(ruleId.toString(), false));
                    continue;
                }
            } else{//rule of include type
                //role
                String isThisRoleIncluded = ruleUserMappingCache.getValue(CacheUtils.getRuleUserKey(ruleId.toString(),RuleUser.TYPE_ROLE,roleId_L.toString()));
                if(isNotNull(isThisRoleIncluded)){
                    filteredRuleKeys.add(CacheUtils.getRuleKey(ruleId.toString(), true));
                    continue;
                }
                //user
                String isThisUserIncluded = ruleUserMappingCache.getValue(CacheUtils.getRuleUserKey(ruleId.toString(), RuleUser.TYPE_USER,userId_L.toString()));
                if(isNotNull(isThisUserIncluded)){
                    filteredRuleKeys.add(CacheUtils.getRuleKey(ruleId.toString(), true));
                    continue;
                }
            }
        }
        if(filteredRuleKeys.isEmpty()){
            return jumpIntercept(invocation);
        }
        String conditionSql = handleRuleList(userId_L.toString(), roleId_L.toString(), filteredRuleKeys, rulesCache);
        /** 应用customize 插件*/
        conditionSql = handlePlugin(conditionSql);
        conditionSqlThisThread.set(conditionSql);
        ThreadLocal<String> threadLocal = new ThreadLocal<>();
        threadLocal.set(conditionSql);
        ThreadLocal<Boolean> threadLocalOfCountFlag = new ThreadLocal<>();
        threadLocalOfCountFlag.set(isCountFlag);
        SqlSource newSqlSource1 = SqlSourceUtil.covertSqlSource(sqlSource, conditionSql,args[1], threadLocal,threadLocalOfCountFlag);
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
                if(isNull(tempSql)){
                    continue;
                }
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
        boolean hasRule = false;
        for(String rule : rulesSet){
            if(isNull(rule) || rule.trim().equals("")){
                continue;
            }
            if(isFirst){
                stringBuilder.append("("+rule+")");
                hasRule = true;
                isFirst = false;
                continue;
            }
            stringBuilder.append(" "+FIELD_SQL_AND+" ("+rule+")");
            hasRule = true;
        }
        stringBuilder.append(")");

        if(hasRule){
            return stringBuilder.toString();
        }
        else {
            return "";
        }
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


    /**
     * callback plugin method, for usage of extending data permission.
     * if doesn't setting external plugin, just ignore;
     * @param conditionSql
     * @return final sql condition
     */
    private String handlePlugin(String conditionSql){
        DataPermissionPluginWrapper pluginWrapper = null;
        try {
            pluginWrapper = applicationContext.getBean(DataPermissionPluginWrapper.class);
        }catch (NoSuchBeanDefinitionException e){
            logger.info("apply no external plugin for data permission");
        }
        if(isNotNull(pluginWrapper)){
            String condition = conditionSql;
            List<DataPermissionPlugin> plugins = pluginWrapper.getPlugins();
            if(isNotNull(plugins) && !plugins.isEmpty()){
                for(DataPermissionPlugin plugin : plugins){
                    condition = plugin.handle(condition);
                }
                conditionSql = condition;
            }
        }

        return conditionSql;
    }

    public static String getConditionSql(){
        return conditionSqlThisThread.get();
    }
}
