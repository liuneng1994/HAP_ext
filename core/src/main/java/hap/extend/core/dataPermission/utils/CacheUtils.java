package hap.extend.core.dataPermission.utils;

/**
 * Created by yyz on 2017/2/16.
 *
 * @author yazheng.yang@hand-china.com
 */
public final class CacheUtils {
    /**
     * 根据数据库中的规则id,
     * 生成redis缓存中的规则key
     * @param ruleId
     * @return
     */
    public static String getRuleKey(final String ruleId){
        return Constant.FIELD_RULE_ID_PREFIX+ruleId;
    }

    /**
     * 根据mapper方法的id,
     * 生成redis缓存中的key,
     * 用于快速找到mapper方法需要应用的规则id数组
     * @param idOfMapperMethod
     * @return
     */
    public static String getMappermethodRulesKey(final String idOfMapperMethod){
        return Constant.FIELD_METHOD_RULE_MAP_PREFIX+idOfMapperMethod;
    }

    /**
     * 生成redis缓存中的key,
     * 用于开始判断当前用户是否在这个规则上生效
     * @param ruleId
     * @param userId
     * @param isInclude
     * @return
     */
    public static String getRuleUserKey(final String ruleId, final String userId, boolean isInclude){
        if(isInclude){
            return Constant.FIELD_RULE_USER_PREFIX+ruleId+"_"+userId+"_Y";
        }
        return Constant.FIELD_RULE_USER_PREFIX+ruleId+"_"+userId+"_N";
    }
}
