/*
 * #{copyright}#
 */

package com.lkkhpg.dsis.platform.vpd.dto;

import com.hand.hap.system.dto.BaseDTO;

import java.util.List;


/**
 * @author chenjingxiong
 */
public class SysVpdAssign extends BaseDTO {
    private Long assignId;

    private Long vpdId;

    private String mapperId;

    private List<SysVpdConfig> vpdConfigs;

    public Long getAssignId() {
        return assignId;
    }

    public void setAssignId(Long assignId) {
        this.assignId = assignId;
    }

    public Long getVpdId() {
        return vpdId;
    }

    public void setVpdId(Long vpdId) {
        this.vpdId = vpdId;
    }

    public String getMapperId() {
        return mapperId;
    }

    public void setMapperId(String mapperId) {
        this.mapperId = mapperId == null ? null : mapperId.trim();
    }

    public List<SysVpdConfig> getVpdConfigs() {
        return vpdConfigs;
    }

    public void setVpdConfigs(List<SysVpdConfig> vpdConfigs) {
        this.vpdConfigs = vpdConfigs;
    }

}