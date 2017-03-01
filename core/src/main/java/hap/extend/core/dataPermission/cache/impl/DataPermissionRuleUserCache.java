package hap.extend.core.dataPermission.cache.impl;

import com.hand.hap.cache.impl.HashStringRedisCache;
import hap.extend.core.dataPermission.dto.RuleUser;
import hap.extend.core.dataPermission.mapper.RuleUserMapper;
import hap.extend.core.dataPermission.utils.CacheUtils;
import hap.extend.core.dataPermission.utils.Constant;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static hap.extend.core.dataPermission.utils.LangUtils.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by yyz on 2017/2/20.
 *
 * @author yazheng.yang@hand-china.com
 */
public class DataPermissionRuleUserCache extends HashStringRedisCache<String> {
    {
        setType(String.class);
    }
    private final static String querySqlId = RuleUserMapper.class.getName()+".selectAll";
    private final Logger logger = LoggerFactory.getLogger(DataPermissionRuleCache.class);

    @Override
    public void init() {
        strSerializer = getRedisTemplate().getStringSerializer();
        initLoad();
    }

    @Override
    public String getValue(String key) {
        return super.getValue(key);
    }

    @Override
    public void setValue(String key, String value) {
        super.setValue(key, value);
    }


    @SuppressWarnings("unchecked")
    protected void initLoad() {
        Set<String> keySets = new HashSet<>();
        try (SqlSession sqlSession = getSqlSessionFactory().openSession()) {
            sqlSession.select(querySqlId, (resultContext) -> {
                RuleUser value = (RuleUser) resultContext.getResultObject();
                if(isNotNull(value)){
                    Long ruleId = value.getRuleId();
                    Long typeValue = value.getTypeId();
                    String type = value.getAssignType();
                    if(isNotNull(ruleId) && isNotNull(typeValue)){
                        keySets.add(CacheUtils.getRuleUserKey(ruleId.toString(),type,typeValue.toString()));
                    }
                }
            });
            keySets.parallelStream().forEach((v)->setValue(v, Constant.VALUE_RULE_USER));
        } catch (Throwable e) {
            if (logger.isErrorEnabled()) {
                logger.error("(hap extend)init rule_user cache exception: ", e);
            }
        }
    }
}
