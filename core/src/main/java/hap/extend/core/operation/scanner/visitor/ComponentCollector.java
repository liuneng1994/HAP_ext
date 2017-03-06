package hap.extend.core.operation.scanner.visitor;

import hap.extend.core.operation.component.HtmlComponent;

import java.util.List;

/**
 * Created by liuneng on 2017/2/21.
 */
public interface ComponentCollector {
    List<HtmlComponent> getComponents();
}
