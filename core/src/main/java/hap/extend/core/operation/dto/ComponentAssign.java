package hap.extend.core.operation.dto;

import com.hand.hap.mybatis.annotation.Condition;
import com.hand.hap.system.dto.BaseDTO;

import javax.persistence.*;

/**
 * mount component to page;<br><br>
 *
 * Created by yyz on 2017/3/10.
 *
 * @author yazheng.yang@hand-china.com
 */
@Table(name = "HCOM_RES_COMPONENT_ASSIGN")
public class ComponentAssign extends BaseDTO {

    @Id
    @GeneratedValue(generator = GENERATOR_TYPE)
    @Column(name = "COMPONENT_ASSIGN_ID")
    private Long cpnAssignId;

    @Column(name = "ASSIGN_ID",nullable = false)
    private Long assignId;

    @Column(name = "COMPONENT_ID",nullable = false)
    private Long componentId;

    @Column(name = "DISPLAY",nullable = false)
    private String display;

    @Column(name = "REQUIRED",nullable = false)
    private String require;

    @Column(name = "READ_ONLY",nullable = false)
    private String readonly;

    @Column(name = "DISABLE",nullable = false)
    private String disable;

    @Column(name = "DESCRIPTION")
    @Condition(operator = LIKE)
    private String description;

    @Column(name = "ENABLE_FLAG",nullable = false)
    @Condition
    private String enableFlag;

    @Transient
    private String componentName;
    @Transient
    private String componentType;


    public Long getCpnAssignId() {
        return cpnAssignId;
    }

    public void setCpnAssignId(Long cpnAssignId) {
        this.cpnAssignId = cpnAssignId;
    }

    public Long getAssignId() {
        return assignId;
    }

    public void setAssignId(Long assignId) {
        this.assignId = assignId;
    }

    public Long getComponentId() {
        return componentId;
    }

    public void setComponentId(Long componentId) {
        this.componentId = componentId;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getRequire() {
        return require;
    }

    public void setRequire(String require) {
        this.require = require;
    }

    public String getReadonly() {
        return readonly;
    }

    public void setReadonly(String readonly) {
        this.readonly = readonly;
    }

    public String getDisable() {
        return disable;
    }

    public void setDisable(String disable) {
        this.disable = disable;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
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
}
