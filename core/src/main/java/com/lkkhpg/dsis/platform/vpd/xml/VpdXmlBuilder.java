/*
 * #{copyright}#
 */

package com.lkkhpg.dsis.platform.vpd.xml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.lkkhpg.dsis.platform.vpd.VpdConfig;

/**
 * XML构建器.
 * @author chenjingxiong.
 */
public class VpdXmlBuilder {

    private Logger logger = LoggerFactory.getLogger(VpdXmlBuilder.class);

    private static final String VPD_ELEMENT_TAG = "vpd";
    private static final String VPD_NAME_TAG = "name";
    private static final String VPD_MAPPER_TAG = "mapper";
    private static final String VPD_SQL_TAG = "sql";

    private static final String USELESS_TEXT_NOTE = "#text";

    private List<VpdConfig> vpdConfigs = new ArrayList<>();

    public VpdXmlBuilder(Resource resource) {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();

            Document doc = builder.parse(resource.getInputStream());
            Node root = doc.getFirstChild();
            NodeList nodes = root.getChildNodes();
            for (int i = 0; i < nodes.getLength(); i++) {
                Node n = nodes.item(i);
                if (VPD_ELEMENT_TAG.equals(n.getNodeName())) {
                    VpdXmlParser parser = new VpdXmlParser(n);
                    vpdConfigs.add(parser.parse());
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            if (logger.isErrorEnabled()) {
                logger.error("error when reading resource: {}", resource, e);
            }
        }

    }

    /**
     * inner xml parser.
     */
    private class VpdXmlParser {

        private Node vpdNode;

        VpdXmlParser(Node vpdNode) {
            this.vpdNode = vpdNode;
        }

        public VpdConfig parse() {
            NodeList children = vpdNode.getChildNodes();

            Map<String, String> m = new HashMap<String, String>();

            for (int j = 0; j < children.getLength(); j++) {
                Node c = children.item(j);
                if (!USELESS_TEXT_NOTE.equals(c.getNodeName())) {
                    m.put(c.getNodeName().toLowerCase(), c.getTextContent().trim());
                }

            }

            String name = m.get(VPD_NAME_TAG);
            String mapper = m.get(VPD_MAPPER_TAG);
            String sql = m.get(VPD_SQL_TAG);

            return new VpdConfig(name, mapper, sql);

        }
    }

    public List<VpdConfig> getVpdConfigs() {
        return vpdConfigs;
    }
}
