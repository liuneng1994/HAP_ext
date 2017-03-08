package hap.extend.core.operation.component;


/**
 * 组件类型
 */
public interface ComponentType {
    public String getType();
    public String getIdPrefix();
    public boolean isThisType(String type);
}
//public class ComponentType {
//    public static String GRID = "grid";
//    public static final Set<String> componentSet;
//
//    static {
//        componentSet = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(GRID)));
//    }
//}
