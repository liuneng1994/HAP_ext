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
 * Created by liuneng on 2017/2/21.
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

    private String getComponentType(String id) {
        String[] strs = id.split("[A-Z]");
        return strs[0];
    }
}
