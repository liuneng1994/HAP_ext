package hap.extend.core.dataPermission.utils;

/**
 * <pre>
 * 数据通用权限用到的常量
 * </pre>
 * Created by yyz on 2017/2/16.
 * @author yazheng.yang@hand-china.com
 */
public final class Constant {
    /** redis缓存中键值对表hap_extend.data_permission的key前缀 */
    public static final String FIELD_METHOD_USER_MAP_PREFIX="EXT_DATA_PERM_";
    /** redis缓存中键值对表hap_extend.rules的key前缀 */
    public static final String FIELD_METHOD_ID_PREFIX="EXT_DATA_PERM_RULE_";
    /** redis键值对表名称,，(key,value)=(key,规则id数组) */
    public static final String FIELD_METHOD_USER_MAP_CACHE_NAME="hap_extend.data_permission";
    /** redis权限规则表名称，(key,value)=(rule_id,规则sql片段) */
    public static final String FIELD_RULES_CACHE_NAME="hap_extend.rules";
}
