package hap.extend.core.dataPermission.cache.impl;

import com.hand.hap.cache.impl.HashStringRedisCache;
import hap.extend.core.dataPermission.dto.Rule;
import hap.extend.core.dataPermission.mapper.RuleMapper;
import hap.extend.core.dataPermission.utils.CacheUtils;
import hap.extend.core.dataPermission.utils.LangUtils;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static hap.extend.core.dataPermission.utils.LangUtils.*;

import java.util.Map;

/**
 * Created by yyz on 2017/2/17.
 *
 * @author yazheng.yang@hand-china.com
 */
public class DataPermissionRuleCache extends HashStringRedisCache<String> {
    {
        setType(String.class);
    }
    private final static String querySqlId = RuleMapper.class.getName()+".selectAll";
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
        try (SqlSession sqlSession = getSqlSessionFactory().openSession()) {
            sqlSession.select(querySqlId, (resultContext) -> {
                Rule rule = (Rule) resultContext.getResultObject();
                if(isNotNull(rule)){
                    Long ruleId = rule.getRuleId();
                    String ruleSql = rule.getRuleSql();
                    String isIncludeType = rule.getIsIncludeType();
                    if(LangUtils.isNotNull(ruleId) && LangUtils.isNotNull(ruleSql)){
                        setValue(CacheUtils.getRuleKey(ruleId.toString(),Rule.isIncludeType(isIncludeType)), ruleSql);
                    }
                }
            });
        } catch (Throwable e) {
            if (logger.isErrorEnabled()) {
                logger.error("(hap extend)init rule cache exception: ", e);
            }
        }
    }
}
