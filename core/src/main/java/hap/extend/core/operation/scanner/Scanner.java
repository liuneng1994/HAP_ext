package hap.extend.core.operation.scanner;

import hap.extend.core.operation.component.HtmlComponent;

import java.io.File;
import java.util.List;

/**
 * 页面组件扫描
 */
public interface Scanner {
    List<HtmlComponent> scan(File html);
}
