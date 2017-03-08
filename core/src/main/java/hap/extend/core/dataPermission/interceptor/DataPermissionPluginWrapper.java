package hap.extend.core.dataPermission.interceptor;

import java.util.List;

/**
 * plugin wrapper in data permission,just hold a list of plugin
 *
 * @author yazheng.yang@hand-china.com
 */
public final class DataPermissionPluginWrapper {
    private List<DataPermissionPlugin> plugins;

    public List<DataPermissionPlugin> getPlugins() {
        return plugins;
    }

    public void setPlugins(List<DataPermissionPlugin> plugins) {
        this.plugins = plugins;
    }
}
