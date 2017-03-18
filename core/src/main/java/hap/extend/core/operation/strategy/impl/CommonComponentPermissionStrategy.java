package hap.extend.core.operation.strategy.impl;

import hap.extend.core.operation.component.HtmlComponent;
import hap.extend.core.operation.strategy.ComponentPermissionStrategy;

/**
 * Created by liuneng on 2017/2/28.
 */
public class CommonComponentPermissionStrategy implements ComponentPermissionStrategy{
    @Override
    public String getPermissionCtrlCode(HtmlComponent component) {
        return null;
    }

    @Override
    public boolean isSupported(HtmlComponent component) {
        return false;
    }
}
