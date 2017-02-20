package hap.extend.core.dataPermission.utils;

/**
 * Created by yyz on 2017/2/16.
 *
 * @author yazheng.yang@hand-china.com
 */
public final class CacheUtils {
    /**
     * 根据数据库中的规则id生成redis缓存中的规则id
     * @param ruleId
     * @return
     */
    public static String getRuleId(final String ruleId){
        return Constant.FIELD_METHOD_ID_PREFIX+ruleId;
    }

    /**
     * 根据mapper方法的id和用户id生成redis缓存中的数据权限规则的id
     * @param idOfMapperMethod
     * @param userId
     * @return
     */
    public static String getRuleIdsOfUserMethodMap(final String idOfMapperMethod, final String userId){
        return Constant.FIELD_METHOD_USER_MAP_PREFIX+idOfMapperMethod+"_"+userId;
    }
}
