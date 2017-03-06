package hap.extend.core.dataPermission.cache.impl;

import com.hand.hap.cache.impl.HashStringRedisCache;
import hap.extend.core.dataPermission.dto.RuleMappermethod;
import hap.extend.core.dataPermission.mapper.RuleMappermethodMapper;
import hap.extend.core.dataPermission.utils.CacheUtils;
import hap.extend.core.dataPermission.utils.LangUtils;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import static hap.extend.core.dataPermission.utils.LangUtils.*;

/**
 * mapper方法对应需要应用的规则id（数据库表中的rule_id）数组
 *
 * Created by yyz on 2017/2/20.
 *
 * @author yazheng.yang@hand-china.com
 */
public class DataPermissionRuleMethodCache extends HashStringRedisCache<Long[]> {
    {
        setType(Long[].class);
    }
    private static final String querySqlId = RuleMappermethodMapper.class.getName()+".selectAllMapping";
    private final Logger logger = LoggerFactory.getLogger(DataPermissionRuleMethodCache.class);


    @Override
    public void init() {
        strSerializer = getRedisTemplate().getStringSerializer();
        clear();
        initLoad();
    }

    @Override
    public Long[] getValue(final String key) {
        return super.getValue(key);
    }

    @Override
    public void setValue(String key, Long[] values) {
        super.setValue(key, values);
    }

    @SuppressWarnings("unchecked")
    protected void initLoad() {
        Map<String, Set<Long>> methodRules = new HashMap<>();
        try (SqlSession sqlSession = getSqlSessionFactory().openSession()) {
            sqlSession.select(querySqlId, (resultContext) -> {
                RuleMappermethod value = (RuleMappermethod) resultContext.getResultObject();

                if(isNotNull(value)){
                    Long ruleId = value.getRuleId();
                    String mapperMethod = value.getSqlId();
                    String enableFlag = value.getEnableFlag();

                    if(LangUtils.isNotNull(ruleId) && LangUtils.isNotNull(mapperMethod) && isNotNull(enableFlag)){
                        Set<Long> sets = methodRules.get(mapperMethod);
                        if(LangUtils.isNull(sets)){
                            sets = new HashSet<Long>();
                            methodRules.put(mapperMethod,sets);
                        }
                        if(RuleMappermethod.isEnable(enableFlag)){
                            sets.add(ruleId);
                        }
                    }
                }
            });

            methodRules.forEach((k, v) -> setValue(CacheUtils.getMappermethodRulesKey(k), v.toArray(new Long[v.size()])));
        } catch (Throwable e) {
            if (logger.isErrorEnabled()) {
                logger.error("init mappermethod_rule cache exception: ", e);
            }
        }
    }

    public boolean addValuesToKey(String key, Long ... values){
        Long[] valuesInCache = getValue(key);
        Set<Long> valuesToAdd = new HashSet<>();
        for (Long v : values){
            if(isNotNull(v) && v >= 0){
                valuesToAdd.add(v);
            }
        }
        if(isNull(valuesInCache)){
            logger.debug("key {} is not exist,and will be added",key);
            setValue(key,valuesToAdd.toArray(new Long[valuesToAdd.size()]));
            return true;
        }
        for(Long v : valuesInCache){
            valuesToAdd.add(v);
        }
        setValue(key,valuesToAdd.toArray(new Long[valuesToAdd.size()]));
        return true;
    }

    public boolean removeValuesFromKey(String key, Long ... values){
        Long[] valuesInCache = getValue(key);
        if(isNull(valuesInCache) || isNull(values) || values.length < 1){
            return true;
        }

        Set<Long> valuesToRemove = new HashSet<>();
        Set<Long> newValues = new HashSet<>();
        for (Long v : values){
            if(isNotNull(v) && v >= 0){
                valuesToRemove.add(v);
            }
        }

        for(Long v : valuesInCache){
            if(!valuesToRemove.contains(v)){
                newValues.add(v);
            }
        }
        setValue(key,newValues.toArray(new Long[newValues.size()]));
        return true;
    }
}
