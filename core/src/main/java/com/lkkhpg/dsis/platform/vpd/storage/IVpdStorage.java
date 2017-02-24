/*
 * #{copyright}#
 */

package com.lkkhpg.dsis.platform.vpd.storage;

import java.util.List;

import com.lkkhpg.dsis.platform.vpd.VpdConfig;

/**
 * @author chenjingxiong on 16/1/5.
 */
public interface IVpdStorage {

    void addVpdConfig(VpdConfig config);

    List<VpdConfig> getVpdConfigs(String mapper);
}
