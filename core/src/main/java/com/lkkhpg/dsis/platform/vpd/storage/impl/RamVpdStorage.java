/*
 * #{copyright}#
 */

package com.lkkhpg.dsis.platform.vpd.storage.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.lkkhpg.dsis.platform.vpd.VpdConfig;
import com.lkkhpg.dsis.platform.vpd.storage.IVpdStorage;

/**
 * vpd配置加载到内存中.
 * @author chenjingxiong on 16/1/5.
 */
public class RamVpdStorage implements IVpdStorage {

    private static Map<String, Set<VpdConfig>> vpdConfigMap = new HashMap<>();

    @Override
    public void addVpdConfig(VpdConfig config) {
        Set<VpdConfig> vpdConfigs = vpdConfigMap.get(config.getMapper());
        if (vpdConfigs == null) {
            vpdConfigs = new HashSet<>();
        }
        vpdConfigs.add(config);
        vpdConfigMap.put(config.getMapper(), vpdConfigs);
    }

    @Override
    public List<VpdConfig> getVpdConfigs(String mapper) {
        List<VpdConfig> vpdConfigs = new ArrayList<>();
        Set<VpdConfig> vpdConfigSet = vpdConfigMap.get(mapper);
        if (vpdConfigSet != null) {
            vpdConfigs.addAll(vpdConfigSet);
        }
        return vpdConfigs;
    }
}
