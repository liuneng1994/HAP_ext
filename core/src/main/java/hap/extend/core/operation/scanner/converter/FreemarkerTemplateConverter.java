package hap.extend.core.operation.scanner.converter;

import hap.extend.core.operation.scanner.marker.Marker;
import hap.extend.core.operation.scanner.marker.freemarker.IncludeMarker;
import org.apache.commons.io.input.CharSequenceInputStream;
import org.apache.commons.io.input.CharSequenceReader;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 将freemarker模板文件转换为合法html
 */
@Component
public class FreemarkerTemplateConverter implements TemplateConverter {
    private List<Marker> markers;
    @PostConstruct
    public void init(){
        markers = new ArrayList<>();
        markers.add(new IncludeMarker());
    }

    @Override
    public String convertToHtml(String template) {
        BufferedReader reader = new BufferedReader(new StringReader(template));
        StringBuilder htmlBuilder = new StringBuilder();
        reader.lines().forEach(line -> {
            for (Marker marker: markers) {
                if (marker.isMarker(line)) {
                    line = marker.getReplacement(line);
//                    htmlBuilder.append(marker.getReplacement(line));
//                    return;
                }
            }
            htmlBuilder.append(line);
        });
        return htmlBuilder.toString();
    }
}
