package hap.extend.core.operation.strategy.impl;

import hap.extend.core.operation.component.HtmlComponent;
import hap.extend.core.operation.strategy.ComponentPermissionStrategy;

/**
 * Created by liuneng on 2017/2/20.
 */
public class ButtonComponentStrategy implements ComponentPermissionStrategy {
    @Override
    public String getPermissionCtrlCode(HtmlComponent component) {
        return null;
    }

    @Override
    public boolean isSupported(HtmlComponent component) {
        return false;
    }
}
