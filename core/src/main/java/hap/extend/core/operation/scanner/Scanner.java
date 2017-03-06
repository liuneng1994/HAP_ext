package hap.extend.core.operation.scanner;

import hap.extend.core.operation.component.HtmlComponent;

import java.io.File;
import java.util.List;

/**
 * Created by liuneng on 2017/2/28.
 */
public interface Scanner {
    List<HtmlComponent> scan(File html);
}
