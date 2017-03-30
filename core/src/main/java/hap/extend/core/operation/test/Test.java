package hap.extend.core.operation.test;

/**
 * Created by yyz on 17/3/23.
 */
public class Test {
    public static void main(String[] args) {
        String level = "GRID";
        String cpnType = "column";
        String opType = "hide";
        String htmlTagId = "grid_divId";
        String format = String.format("%s_%s_%sFunction(%s)", level, cpnType, opType, htmlTagId);
        System.out.println(format);
    }
}
