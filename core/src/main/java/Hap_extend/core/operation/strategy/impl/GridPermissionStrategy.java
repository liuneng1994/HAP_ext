package Hap_extend.core.operation.strategy.impl;

import Hap_extend.core.operation.component.HtmlComponent;
import Hap_extend.core.operation.strategy.ComponentPermissionStrategy;

/**
 * Created by liuneng on 2017/2/20.
 */
public class GridPermissionStrategy implements ComponentPermissionStrategy {
    @Override
    public String getPermissionCtrlCode(HtmlComponent component) {
        return null;
    }

    @Override
    public boolean isSupported(HtmlComponent component) {
        return false;
    }
}
