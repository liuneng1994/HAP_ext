package hap.extend.core.operation.utils;

/**
 * Created by yyz on 2017/3/15.
 *
 * @author yazheng.yang@hand-china.com
 */
public final class LangUtil {
    public static boolean isNull(Object obj){
        return null == obj;
    }

    public static boolean isNotNull(Object object){
        return null != object;
    }

    public static String returnNullIfStrEmpty(String str){
        if(isNull(str) || str.isEmpty()){
            return null;
        }
        return str;
    }
}
