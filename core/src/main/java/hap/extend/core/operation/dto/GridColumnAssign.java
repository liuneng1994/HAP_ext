package hap.extend.core.operation.dto;

import com.hand.hap.mybatis.annotation.Condition;
import com.hand.hap.system.dto.BaseDTO;

import javax.persistence.*;

/**
 * Created by yyz on 17/4/1.
 */
@Table(name = "HCOM_RES_COLUMN_ASSIGN")
public class GridColumnAssign extends BaseDTO {

    @Id
    @GeneratedValue(generator = GENERATOR_TYPE)
    @Column(name = "COLUMN_ASSIGN_ID")
    private Long columnAssignId;

    @Column(name = "COMPONENT_ASSIGN_ID",nullable = false)
    private Long cpnAssignId;

    @Column(name = "CPN_COLUMN_ID")
    private Long columnId;


    @Column(name = "DISPLAY",nullable = false)
    private String display;

    @Column(name = "REQUIRED")
    private String require;

    @Column(name = "READ_ONLY",nullable = false)
    private String readonly;

    @Column(name = "DISABLE")
    private String disable;

    @Column(name = "DESCRIPTION")
    @Condition(operator = LIKE)
    private String description;

    @Column(name = "ENABLE_FLAG",nullable = false)
    @Condition
    private String enableFlag;

    @Transient
    private String columnName;
    @Transient
    private Long columnIndex;


    public Long getColumnAssignId() {
        return columnAssignId;
    }

    public void setColumnAssignId(Long columnAssignId) {
        this.columnAssignId = columnAssignId;
    }

    public Long getCpnAssignId() {
        return cpnAssignId;
    }

    public void setCpnAssignId(Long cpnAssignId) {
        this.cpnAssignId = cpnAssignId;
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

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public Long getColumnId() {
        return columnId;
    }

    public void setColumnId(Long columnId) {
        this.columnId = columnId;
    }

    public Long getColumnIndex() {
        return columnIndex;
    }

    public void setColumnIndex(Long columnIndex) {
        this.columnIndex = columnIndex;
    }
}
