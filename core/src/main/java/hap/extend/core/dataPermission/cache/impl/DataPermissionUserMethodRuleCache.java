package hap.extend.core.dataPermission.cache.impl;

import com.hand.hap.cache.impl.HashStringRedisCache;
import com.hand.hap.function.mapper.RoleFunctionMapper;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * redis缓存，用于存储用户、mapper methodId对应的规则id的数组
 * Created by yyz on 2017/2/17.
 *
 * @author yazheng.yang@hand-china.com
 */
public class DataPermissionUserMethodRuleCache extends HashStringRedisCache<Long[]> {
    {
        setType(Long[].class);
    }

    private String roleFunctionQuerySqlId = RoleFunctionMapper.class.getName() + ".selectAllRoleFunctions";

    private final Logger logger = LoggerFactory.getLogger(DataPermissionUserMethodRuleCache.class);


    @Override
    public void init() {
        strSerializer = getRedisTemplate().getStringSerializer();
        initLoad();
    }
    /**
     * key 为roleId.
     *
     * @param key
     *            roleId
     * @return values
     */
    @Override
    public Long[] getValue(String key) {
        return super.getValue(key);
    }

    /**
     *
     * @param key
     *            code.lang
     * @param values values
     */
    @Override
    public void setValue(String key, Long[] values) {
        super.setValue(key, values);
    }

    @SuppressWarnings("unchecked")
    protected void initLoad() {
        Map<String, Set<Long>> roleFunctions = new HashMap<>();
        try (SqlSession sqlSession = getSqlSessionFactory().openSession()) {
            sqlSession.select(roleFunctionQuerySqlId, (resultContext) -> {
                Map<String, Object> value = (Map<String, Object>) resultContext.getResultObject();
                String roleId = "" + value.get("ROLE_ID");
                Set<Long> sets = roleFunctions.get(roleId);
                if (sets == null) {
                    sets = new HashSet<>();
                    roleFunctions.put(roleId, sets);
                }
                Long resourceId = ((Number) value.get("FUNCTION_ID")).longValue();
                sets.add(resourceId);
            });

            roleFunctions.forEach((k, v) -> {
                setValue(k, v.toArray(new Long[v.size()]));
            });
        } catch (Throwable e) {
            if (logger.isErrorEnabled()) {
                logger.error("init role function cache exception: ", e);
            }
        }
    }

}
