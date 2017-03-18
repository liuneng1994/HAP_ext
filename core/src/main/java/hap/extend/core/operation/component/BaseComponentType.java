package hap.extend.core.operation.component;

/**
 * 组件类别基础类
 * </br>
 * Created by yyz on 2017/3/8.
 * @author yazheng.yang@hand-china.com
 */
public class BaseComponentType implements ComponentType {
    private String type;
    private String idPrefix;
    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public String getIdPrefix() {
        return this.idPrefix;
    }

    @Override
    public boolean isThisType(String type) {
        if(null == this.type){
            return false;
        }
        return this.type.equals(type);
    }
}
