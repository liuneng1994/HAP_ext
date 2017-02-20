package Hap_extend.core.operation.strategy;

import Hap_extend.core.operation.component.HtmlComponent;

/**
 * Created by liuneng on 2017/2/20.
 */

/**
 * 权限控制策略
 */
public interface ComponentPermissionStrategy {
    /**
     * 生成权限控制代码
     * @param component 控件信息
     * @return 权限控制代码
     */
    String getPermissionCtrlCode(HtmlComponent component);

    /**
     * 是否支持此控件
     * @param component
     * @return
     */
    boolean isSupported(HtmlComponent component);

    /**
     * 提供静默的代码生成调用,如果不支持返回
     * @param component
     * @return
     */
    default String safeGetCode(HtmlComponent component) {
        if (isSupported(component)) {
            return getPermissionCtrlCode(component);
        }
        return "";
    }
}
