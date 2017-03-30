package hap.extend.core.operation.dto;

import com.hand.hap.mybatis.annotation.Condition;
import com.hand.hap.system.dto.BaseDTO;
import hap.extend.core.operation.utils.OPConstUtil;

import javax.persistence.*;

/**
 * each component in single page. <br>
 * <br>
 * Created by yyz on 2017/3/10.
 * @author yazheng.yang@hand-china.com
 */
@Table(name = "HCOM_RES_COMPONENT")
public class Component extends BaseDTO {
    @Id
    @GeneratedValue(generator = GENERATOR_TYPE)
    @Column(name = "COMPONENT_ID")
    private Long componentId;

    @Column(name = "RESOURCE_ID",nullable = false)
    @Condition
    private Long resourceId;

    @Transient
    private String resourceUri;

    @Column(name = "COMPONENT_NAME",nullable = false)
    @Condition(operator = LIKE)
    private String componentName;

    @Column(name = "COMPONENT_TYPE",nullable = false)
    @Condition(operator = LIKE)
    private String componentType;

    @Column(name = "DESCRIPTION")
    @Condition(operator = LIKE)
    private String description;

    /** GRID OR FORM*/
    @Column(name = "LEVEL",nullable = false)
    @Condition(operator = LIKE)
    private String level;

//    @Column(name = "COMPONENTID_CODE",nullable = false)
//    @Condition(operator = LIKE)
//    private String htmlTagId;

    /** OP_PMS_NAME or ID now,uppercase.this is attribute of html tag
     * @see OPConstUtil#HTML_TAG_ATTR_ID
     * @see OPConstUtil#HTML_TAG_ATTR_OP_PMS_NAME
     * */
    @Column(name = "HTML_TAG_ATTR",nullable = false)
    @Condition
    private String htmlTagAttr;
    @Column(name = "HTML_TAG_ATTR_VAL",nullable = false)
    @Condition
    private String htmlTagAttrVal;

    @Column(name = "ENABLE_FLAG",nullable = false)
    @Condition
    private String enableFlag;

    public Long getComponentId() {
        return componentId;
    }

    public void setComponentId(Long componentId) {
        this.componentId = componentId;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceUri() {
        return resourceUri;
    }

    public void setResourceUri(String resourceUri) {
        this.resourceUri = resourceUri;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLevel() {
        return level;
    }

    public String getHtmlTagAttr() {
        return htmlTagAttr;
    }

    public void setHtmlTagAttr(String htmlTagAttr) {
        this.htmlTagAttr = htmlTagAttr;
    }

    public String getHtmlTagAttrVal() {
        return htmlTagAttrVal;
    }

    public void setHtmlTagAttrVal(String htmlTagAttrVal) {
        this.htmlTagAttrVal = htmlTagAttrVal;
    }

    public void setLevel(String level) {
        this.level = level;
    }

//    public String getHtmlTagId() {
//        return htmlTagId;
//    }
//
//    public void setHtmlTagId(String htmlTagId) {
//        this.htmlTagId = htmlTagId;
//    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }


}
