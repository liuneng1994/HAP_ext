package hap.extend.core.operation.utils;

/**
 * Created by yyz on 2017/3/10.
 *
 * @author yazheng.yang@hand-china.com
 */
public final class OPConstUtil {
    public static final String VALUE_YES = "Y";
    public static final String VALUE_NO = "N";
    public static final String VALUE_ROLE_TYPE = "ROLE";
    public static final String VALUE_USER_TYPE = "USER";
    public static final String VALUE_GLOBAL_TYPE = "GLOBAL";


    public static boolean isEnable(String enableFlagStr){
        return VALUE_YES.equals(enableFlagStr);
    }
    public static boolean isDisable(String enableFlagStr){
        return VALUE_NO.equals(enableFlagStr);
    }
}
