package hap.extend.core.operation.utils;

import static hap.extend.core.operation.utils.LangUtil.isNotNull;
import static hap.extend.core.operation.utils.LangUtil.isNull;

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
    public static final String VALUE_GRID_CPN_TYPE = "grid";
    public static final String VALUE_FORM_LEVEL = "FORM";
    public static final String JS_FUNCTION_NAME_FORMAT = "%s_%s_%sFunction";
    public static final String GRID_COLUMN_DISPLAY_FUNCTION_NAME_FORMAT = "GRID_grid_column_displayFunction('%s','%s',%s)";
    public static final String GRID_COLUMN_READONLY_FUNCTION_NAME_FORMAT = "GRID_grid_column_readonlyFunction('%s','%s',%s)";
    public static final String JS_COMMON_FUNCTION_NAME_FORMAT = "COMMON_%s_%sFunction";
    public static final String JS_CALLBACK_CODE_FORMAT = "\nif('undefined' != typeof(%s)){%s('%s','%s','%s');}else {%s('%s','%s','%s');}\n";
    public static final String GRID_COLUMN_CALLBACK_CODE_FORMAT = "\n%s;%s;\n";

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


    public static String generateHideJsFunName(String htmlTagAttr, String htmlTagAttrVal, String hideColumnsIndexArr){
        if(isNull(hideColumnsIndexArr) || "".equals(hideColumnsIndexArr)){
            return null;
        }
        return String.format(GRID_COLUMN_DISPLAY_FUNCTION_NAME_FORMAT, htmlTagAttr,htmlTagAttrVal,hideColumnsIndexArr);
    }

    public static String generateForbidEditJsFunName(String htmlTagAttr, String htmlTagAttrVal, String forbidEditColumnsIndexArr){
        if(isNull(forbidEditColumnsIndexArr) || "".equals(forbidEditColumnsIndexArr)){
            return null;
        }
        return String.format(GRID_COLUMN_READONLY_FUNCTION_NAME_FORMAT, htmlTagAttr,htmlTagAttrVal,forbidEditColumnsIndexArr);
    }

    /**
     * generate js code for grid column specially.
     * @param htmlTagAttr
     * @param htmlTagAttrVal
     * @param hideColumnsIndexArr
     * @param forbidEditColumnsIndexArr
     * @return
     */
    public static String generateColumnJsCode(String htmlTagAttr, String htmlTagAttrVal, String hideColumnsIndexArr, String forbidEditColumnsIndexArr){
        String hideJs = generateHideJsFunName(htmlTagAttr,htmlTagAttrVal,hideColumnsIndexArr);
        String forbidEditJs = generateForbidEditJsFunName(htmlTagAttr,htmlTagAttrVal,forbidEditColumnsIndexArr);
        if(isNull(hideJs) || "".equals(forbidEditJs)){
            if(isNotNull(forbidEditJs) && !"".equals(forbidEditJs)){
                return forbidEditJs+";";
            }
        }else {
            if(isNotNull(forbidEditJs) && !"".equals(forbidEditJs)){
                return String.format(GRID_COLUMN_CALLBACK_CODE_FORMAT,hideJs,forbidEditJs);
            }else {
                return hideJs+";";
            }
        }
        return "";
    }


    public static boolean isGridgridType(String level, String cpnType){
        if(VALUE_GRID_LEVEL.equalsIgnoreCase(level) && VALUE_GRID_CPN_TYPE.equals(cpnType)){
            return true;
        }
        return false;
    }
}
