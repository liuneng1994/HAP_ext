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

    public static final String VALUE_GRID_LEVEL = "GRID";
    public static final String VALUE_FORM_LEVEL = "FORM";
    public static final String JS_FUNCTION_NAME_FORMAT = "%s_%s_%sFunction";
    public static final String JS_COMMON_FUNCTION_NAME_FORMAT = "COMMON_%s_%sFunction";
    public static final String JS_CALLBACK_CODE_FORMAT = "\nif('undefined' != typeof(%s)){%s('%s','%s','%s');}else {%s('%s','%s','%s');}\n";

    public static final String HTML_TAG_ATTR_ID = "ID";
    public static final String HTML_TAG_ATTR_OP_PMS_NAME = "OP_PMS_NAME";

    public static final String CPN_CTR_FUN_TYPE_DISPLAY = "display";
    public static final String CPN_CTR_FUN_TYPE_REQUIRED = "require";
    public static final String CPN_CTR_FUN_TYPE_READONLY = "readonly";
    public static final String CPN_CTR_FUN_TYPE_DISABLE = "disable";



    public static boolean isEnable(String enableFlagStr){
        return VALUE_YES.equals(enableFlagStr);
    }
    public static boolean isDisable(String enableFlagStr){
        return VALUE_NO.equals(enableFlagStr);
    }

    /**
     * generate the js function name of special format
     * @param level
     * @param cpnType
     * @param opType -- visible/require/readonly/disable
     * @return js function name
     */
    public static String generateJsFunName(String level, String cpnType, String opType){
        return String.format(JS_FUNCTION_NAME_FORMAT, level, cpnType, opType);
    }

    /**
     * generate the common js function name of special format
     * @param level
     * @param opType -- visible/require/readonly/disable
     * @return js function name
     */
    public static String generateJsCommonFunName(String level, String opType){
        return String.format(JS_COMMON_FUNCTION_NAME_FORMAT, level, opType);
    }

    /**
     * generate js code for each component limited by each op type.
     * @param level
     * @param cpnType
     * @param opType
     * @param htmlTagAttr
     * @param htmlTagAttrVal
     * @return
     */
    public static String generateJsCode(String level, String cpnType, String opType, String htmlTagAttr, String htmlTagAttrVal, String yesOrNo){
        String selfDefineJs = generateJsFunName(level,cpnType,opType);
        String commonJs = generateJsCommonFunName(level,opType);
        return String.format(JS_CALLBACK_CODE_FORMAT, selfDefineJs, selfDefineJs, htmlTagAttr, htmlTagAttrVal, yesOrNo, commonJs, htmlTagAttr, htmlTagAttrVal, yesOrNo);
    }
}
