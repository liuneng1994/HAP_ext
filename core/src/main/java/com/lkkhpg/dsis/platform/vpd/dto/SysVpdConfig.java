/*
 * #{copyright}#
 */

package com.lkkhpg.dsis.platform.vpd.dto;

import com.hand.hap.system.dto.BaseDTO;

import java.util.List;

/**
 * @author chenjingxiong
 */
public class SysVpdConfig extends BaseDTO {
    private Long vpdId;

    private String vpdName;

    private String tableName;

    private String description;

    private List<SysVpdDetails> vpdDetails;

    public Long getVpdId() {
        return vpdId;
    }

    public void setVpdId(Long vpdId) {
        this.vpdId = vpdId;
    }

    public String getVpdName() {
        return vpdName;
    }

    public void setVpdName(String vpdName) {
        this.vpdName = vpdName == null ? null : vpdName.trim();
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName == null ? null : tableName.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public List<SysVpdDetails> getVpdDetails() {
        return vpdDetails;
    }

    public void setVpdDetails(List<SysVpdDetails> vpdDetails) {
        this.vpdDetails = vpdDetails;
    }

}