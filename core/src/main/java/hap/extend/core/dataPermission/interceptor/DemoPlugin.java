package hap.extend.core.dataPermission.interceptor;

/**
 * demo for usage of {@link DataPermissionPlugin}
 * <br/>
 * Created by yyz on 2017/3/2.
 *
 * @author yazheng.yang@hand-china.com
 */
public class DemoPlugin implements DataPermissionPlugin {
    @Override
    public String handle(String conditionSql) {
        return conditionSql;
    }
}
