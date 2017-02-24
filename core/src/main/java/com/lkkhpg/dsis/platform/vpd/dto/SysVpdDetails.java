/*
 * #{copyright}#
 */

package com.lkkhpg.dsis.platform.vpd.dto;


import com.hand.hap.system.dto.BaseDTO;

/**
 * @author chenjingxiong
 */
public class SysVpdDetails extends BaseDTO {
    public static final String OPERATION_EQUALS = "E";

    public static final String OPERATION_IN = "IN";

    public static final String OPERATION_NOT_EQUALS = "NE";

    public static final String OPERATION_GT = "GT";

    public static final String OPERATION_GE = "GE";

    public static final String OPERATION_LT = "LT";

    public static final String OPERATION_LE = "LE";

    private Long vpdDetailId;

    private Long vpdId;

    private String columnName;

    private String operation;

    private String expression;

    public Long getVpdDetailId() {
        return vpdDetailId;
    }

    public void setVpdDetailId(Long vpdDetailId) {
        this.vpdDetailId = vpdDetailId;
    }

    public Long getVpdId() {
        return vpdId;
    }

    public void setVpdId(Long vpdId) {
        this.vpdId = vpdId;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName == null ? null : columnName.trim();
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation == null ? null : operation.trim();
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression == null ? null : expression.trim();
    }

}