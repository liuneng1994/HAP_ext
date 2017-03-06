package hap.extend.core.operation.scanner.marker;

/**
 * Created by liuneng on 2017/2/21.
 */

/**
 * 模板标记
 */
public interface Marker {
    boolean isMarker(String marker);
    String getReplacement(String marker);
}
