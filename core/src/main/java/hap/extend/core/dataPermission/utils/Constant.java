package hap.extend.core.dataPermission.utils;

/**
 * <pre>
 * 数据通用权限用到的常量
 * </pre>
 * Created by yyz on 2017/2/16.
 * @author yazheng.yang@hand-china.com
 */
public final class Constant {
    /** redis mapper方法对应于哪些规则的键值对表名称，(key,value)=(key,规则id数组) */
    public static final String FIELD_METHOD_RULES_MAP_CACHE_NAME ="hap_extend.mappermethod_rules";
    /** redis权限规则表名称，(key,value)=(rule_id,规则sql片段) */
    public static final String FIELD_RULES_CACHE_NAME="hap_extend.rules";
    /** redis 规则是否需要应用到用户的映射表名称，(key,value)=(包含rule_id、user_id、是否应用标志位,字符串"Y") */
    public static final String FIELD_RULE_USER_CACHE_NAME="hap_extend.rule_user";

    /** redis缓存中键值对表hap_extend.data_permission的key前缀 */
    public static final String FIELD_METHOD_RULE_MAP_PREFIX="RULES_";
    /** redis缓存中键值对表hap_extend.rules的key前缀 */
    public static final String FIELD_RULE_ID_PREFIX="RULE_";
    /** redis缓存中键值对表hap_extend.rule_user的key前缀 */
    public static final String FIELD_RULE_USER_PREFIX="RULE_USER_";

    public static final String VALUE_RULE_USER = "Y";

}
