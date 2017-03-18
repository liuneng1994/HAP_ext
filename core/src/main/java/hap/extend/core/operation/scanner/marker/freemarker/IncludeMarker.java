package hap.extend.core.operation.scanner.marker.freemarker;

import hap.extend.core.operation.scanner.marker.Marker;


/**
 * freemarker中的include指令
 */
public class IncludeMarker implements Marker {
    private static String markerPattern = "<#include \"[\\s\\S]*\">";
    private static String replacement = "<html>";

    @Override
    public boolean isMarker(String marker) {
        return marker.matches(markerPattern);
    }

    @Override
    public String getReplacement(String marker) {
        return marker.replaceAll(markerPattern,replacement);
    }
}
