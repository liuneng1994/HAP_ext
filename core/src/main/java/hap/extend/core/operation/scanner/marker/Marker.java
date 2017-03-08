package hap.extend.core.operation.scanner.marker;

/**
 * 模板标记。用于判断是否包含特殊标签，以及特殊标签替换成标准HTML代码片段
 */
public interface Marker {
    boolean isMarker(String marker);
    String getReplacement(String marker);
}
