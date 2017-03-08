package hap.extend.core.operation.component;

/**
 * Created by yyz on 2017/3/8.
 *
 * @author yazheng.yang@hand-china.com
 */
public class ButtonComponentType implements ComponentType {
    public static final String TYPE="BUTTON";
    public static final String ID_PREFIX="button";

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public String getIdPrefix() {
        return ID_PREFIX;
    }

    @Override
    public boolean isThisType(String type) {
        return TYPE.equals(type);
    }
}
