package hap.extend.core.operation.component;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by liuneng on 2017/2/20.
 */
public class ComponentType {
    public static String GRID = "grid";
    public static final Set<String> componentSet;

    static {
        componentSet = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(GRID)));
    }
}
