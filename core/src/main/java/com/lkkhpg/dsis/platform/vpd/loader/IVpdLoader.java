/*
 * #{copyright}#
 */

package com.lkkhpg.dsis.platform.vpd.loader;

import java.util.List;

import com.lkkhpg.dsis.platform.vpd.VpdConfig;

/**
 * Vpd Loader.
 * @author chenjingxiong on 16/1/5.
 */
public interface IVpdLoader {
    /**
     * 通过执行的mapper方法获取vpd设置.
     * 
     * @param mapper mapper
     * @return vpd设置列表.
     */
    List<VpdConfig> getVpdConfigs(String mapper);
}
