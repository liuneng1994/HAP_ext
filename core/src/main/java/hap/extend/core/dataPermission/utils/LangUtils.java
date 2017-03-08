package hap.extend.core.dataPermission.utils;

/**
 * Created by yyz on 2017/2/20.
 *
 * @author yazheng.yang@hand-china.com
 */
public class LangUtils {

    public static boolean isNull(Object object){
        return null == object;
    }

    public static boolean isNotNull(Object object){
        return null != object;
    }

    public static boolean isStrEmpty(String str){
        if(null == str){
            return true;
        }
        return str.isEmpty();
    }
}
