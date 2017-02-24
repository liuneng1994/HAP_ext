/*
 * #{copyright}#
 */

package com.lkkhpg.dsis.platform.vpd.loader.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import com.lkkhpg.dsis.platform.vpd.VpdConfig;
import com.lkkhpg.dsis.platform.vpd.loader.IVpdLoader;
import com.lkkhpg.dsis.platform.vpd.storage.IVpdStorage;
import com.lkkhpg.dsis.platform.vpd.xml.VpdXmlBuilder;

/**
 * 读取XML VPD配置.
 * 
 * @author chenjingxiong on 16/1/5.
 */
public class XmlVpdLoader implements IVpdLoader {

    private Logger logger = LoggerFactory.getLogger(XmlVpdLoader.class);

    private Resource[] vpdConfigLocations;

    private IVpdStorage vpdStorage;

    public void init() {
        if (logger.isInfoEnabled()) {
            logger.info("loading vpd with resources:{}", vpdConfigLocations);
        }
        for (Resource vpdConfigLocation : vpdConfigLocations) {
            VpdXmlBuilder builder = new VpdXmlBuilder(vpdConfigLocation);
            List<VpdConfig> vpdConfigs = builder.getVpdConfigs();
            for (VpdConfig config : vpdConfigs) {
                vpdStorage.addVpdConfig(config);
            }
        }
    }

    @Override
    public List<VpdConfig> getVpdConfigs(String mapper) {
        return vpdStorage.getVpdConfigs(mapper);
    }

    public IVpdStorage getVpdStorage() {
        return vpdStorage;
    }

    public void setVpdStorage(IVpdStorage vpdStorage) {
        this.vpdStorage = vpdStorage;
    }

    public void setVpdConfigLocations(Resource[] vpdConfigLocations) {
        this.vpdConfigLocations = vpdConfigLocations;
    }
}
