package hap.extend.core.dataPermission.interceptor;

/**
 * plugin for sur-handle condition sql.customize plugin
 *
 * Created by yyz on 2017/3/2.
 *
 * @author yazheng.yang@hand-china.com
 */
public interface DataPermissionPlugin {
    public String handle(String conditionSql);
}
