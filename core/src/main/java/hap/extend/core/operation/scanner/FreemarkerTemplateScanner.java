package hap.extend.core.operation.scanner;

import hap.extend.core.operation.component.HtmlComponent;
import hap.extend.core.operation.scanner.converter.FreemarkerTemplateConverter;
import hap.extend.core.operation.scanner.visitor.ElementIdVisitor;
import org.apache.commons.io.FileUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import java.io.*;
import java.util.*;

/**
 * Freemarker模板文件组件扫描
 */
@Component
public class FreemarkerTemplateScanner implements Scanner{
    @Autowired
    private FreemarkerTemplateConverter freemarkerTemplateConverter;
    private SAXReader saxReader;
    @PostConstruct
    public void init() {
        saxReader = new SAXReader();
    }

    public List<HtmlComponent> scan(File file) {
        String templateContent;
        List<HtmlComponent> components;
        try {
            templateContent = FileUtils.readFileToString(file);
            String htmlContent = freemarkerTemplateConverter.convertToHtml(templateContent);
            components = doScan(htmlContent);
        } catch (IOException e) {
            throw new RuntimeException("模板文件读取失败",e);
        }
        return components;
    }

    private List<HtmlComponent> doScan(String html) {
        List<HtmlComponent> components = new ArrayList<>();
        try {
            Document document = saxReader.read(new StringReader(html));
            Element root = document.getRootElement();
            ElementIdVisitor visitor = new ElementIdVisitor();
            root.accept(visitor);
            components = visitor.getComponents();
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
        return components;
    }

}
