package hap.extend.core.operation.scanner.visitor;

import hap.extend.core.operation.component.HtmlComponent;
import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.Visitor;
import org.dom4j.VisitorSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 用于从HTML中解析出组件的ID以及组件类型.
 *
 * <pre>
 *主要思路：从解析的dom对象中获取ID，对其进行解析(字符串截取)
 * </pre>
 */
public class ElementIdVisitor extends VisitorSupport implements ComponentCollector{
    private List<HtmlComponent> components = new ArrayList<>();
    private Pattern pattern = Pattern.compile("^([a-z]*)");

    @Override
    public void visit(Element node) {
        super.visit(node);
        Attribute id = node.attribute("id");
        if (id != null) {
            HtmlComponent component = new HtmlComponent();
            component.setId(id.getValue());
            component.setType(getComponentType(id.getValue()));
            components.add(component);
        }
    }

    @Override
    public List<HtmlComponent> getComponents() {
        return components;
    }

    //TODO 后期需要修改这个规则，使用下划线进行分隔
    private String getComponentType(String id) {
        String[] strs = id.split("[A-Z]");
        return strs[0];
    }
}
