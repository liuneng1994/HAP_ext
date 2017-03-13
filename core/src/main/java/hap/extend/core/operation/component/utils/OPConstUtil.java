package hap.extend.core.operation.component.utils;

/**
 * Created by yyz on 2017/3/10.
 *
 * @author yazheng.yang@hand-china.com
 */
public final class OPConstUtil {
    public static final String VALUE_YES = "Y";
    public static final String VALUE_NO = "N";

    public static boolean isEnable(String enableFlagStr){
        return VALUE_YES.equals(enableFlagStr);
    }
    public static boolean isDisable(String enableFlagStr){
        return VALUE_NO.equals(enableFlagStr);
    }
}
