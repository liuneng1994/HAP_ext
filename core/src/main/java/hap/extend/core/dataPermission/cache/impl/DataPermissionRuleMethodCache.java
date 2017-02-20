package hap.extend.core.dataPermission.cache.impl;

import com.hand.hap.cache.impl.HashStringRedisCache;
import hap.extend.core.dataPermission.mapper.RuleMappermethodMapper;
import hap.extend.core.dataPermission.utils.LangUtils;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * mapper方法对应需要应用的规则id（数据库表中的rule_id）数组
 *
 * Created by yyz on 2017/2/20.
 *
 * @author yazheng.yang@hand-china.com
 */
public class DataPermissionRuleMethodCache  extends HashStringRedisCache<Long[]> {

    private static final String querySqlId = RuleMappermethodMapper.class.getName()+".selectAll";
    private final Logger logger = LoggerFactory.getLogger(DataPermissionRuleMethodCache.class);


    @Override
    public void init() {
        strSerializer = getRedisTemplate().getStringSerializer();
        initLoad();
    }

    @Override
    public Long[] getValue(String key) {
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
                Map<String, Object> value = (Map<String, Object>) resultContext.getResultObject();
                Object ruleIdObj = value.get("RULE_ID");
                Object methodObj = value.get("MAPPER_METHOD");
                if(LangUtils.isNotNull(ruleIdObj) && LangUtils.isNotNull(methodObj)){
                    Set<Long> sets = methodRules.get(ruleIdObj.toString());
                    if(LangUtils.isNull(sets)){
                        sets = new HashSet<Long>();
                        methodRules.put(ruleIdObj.toString(),sets);
                    }
                    sets.add((Long)methodObj);
                }
            });

            methodRules.forEach((k, v) -> setValue(k, v.toArray(new Long[v.size()])));
        } catch (Throwable e) {
            if (logger.isErrorEnabled()) {
                logger.error("init mappermethod_rule cache exception: ", e);
            }
        }
    }
}
