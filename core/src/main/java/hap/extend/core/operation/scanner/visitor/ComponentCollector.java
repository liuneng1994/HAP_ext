package hap.extend.core.operation.scanner.visitor;

import hap.extend.core.operation.component.HtmlComponent;

import java.util.List;

/**
 * 页面组件收集器
 */
public interface ComponentCollector {
    List<HtmlComponent> getComponents();
}
