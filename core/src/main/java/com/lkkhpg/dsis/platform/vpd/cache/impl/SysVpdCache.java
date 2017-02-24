/*
 * #{copyright}#
 */
package com.lkkhpg.dsis.platform.vpd.cache.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hand.hap.cache.impl.HashStringRedisCache;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lkkhpg.dsis.platform.vpd.dto.SysVpdAssign;
import com.lkkhpg.dsis.platform.vpd.dto.SysVpdConfig;
import com.lkkhpg.dsis.platform.vpd.dto.SysVpdDetails;
import com.lkkhpg.dsis.platform.vpd.mapper.SysVpdAssignMapper;
import com.lkkhpg.dsis.platform.vpd.mapper.SysVpdDetailsMapper;

/**
 * @author chenjingxiong
 */
public class SysVpdCache extends HashStringRedisCache<SysVpdAssign> {
    
    private Logger logger = LoggerFactory.getLogger(SysVpdCache.class);

    private static final String VPD_ASSIGN_SQL_ID = SysVpdAssignMapper.class.getName() + ".selectAllVpdWithAssign";

    private static final String VPD_CONFIG_DETAIL_SQL_ID = SysVpdDetailsMapper.class.getName()
            + ".selectDetailByVpdConfig";

    @Override
    public void init() {
        setType(SysVpdAssign.class);
        strSerializer = getRedisTemplate().getStringSerializer();
        initLoad();
    }

    protected void initLoad() {
        Map<String, List<SysVpdConfig>> map = new HashMap<>();
        try (SqlSession sqlSession = getSqlSessionFactory().openSession()) {
            sqlSession.select(VPD_ASSIGN_SQL_ID, (assignResultContext) -> {
                int count = assignResultContext.getResultCount();
                List<Map<String, Object>> sysVpdAssigns = null;
                if (count == 1) {
                    sysVpdAssigns = new ArrayList<>();
                    sysVpdAssigns.add((Map<String, Object>) assignResultContext.getResultObject());
                } else if (count > 1) {
                    sysVpdAssigns = (List<Map<String, Object>>) assignResultContext.getResultObject();
                }

                if (sysVpdAssigns != null) {
                    for (Map<String, Object> sysVpdAssign : sysVpdAssigns) {
                        String mapperId = (String) sysVpdAssign.get("MAPPER_ID");
                        List<SysVpdConfig> sysVpdConfigs = map.get(mapperId);
                        if (sysVpdConfigs == null) {
                            sysVpdConfigs = new ArrayList<>();
                        }
                        SysVpdConfig sysVpdConfig = new SysVpdConfig();

                        String vpdName = (String) sysVpdAssign.get("VPD_NAME");
                        String tableName = (String) sysVpdAssign.get("TABLE_NAME");
                        String description = (String) sysVpdAssign.get("DESCRIPTION");
                        BigDecimal vpdId = (BigDecimal) sysVpdAssign.get("VPD_ID");
                        sysVpdConfig.setVpdId(vpdId.longValue());
                        sysVpdConfig.setVpdName(vpdName);
                        sysVpdConfig.setDescription(description);
                        sysVpdConfig.setTableName(tableName);

                        sqlSession.select(VPD_CONFIG_DETAIL_SQL_ID, sysVpdConfig.getVpdId(), (detailsResultContext) -> {
                            int detailsCount = detailsResultContext.getResultCount();
                            List<SysVpdDetails> vpdDetails = null;
                            if (detailsCount == 1) {
                                vpdDetails = new ArrayList<>();
                                vpdDetails.add((SysVpdDetails) detailsResultContext.getResultObject());
                            } else if (detailsCount > 1) {
                                vpdDetails = (List<SysVpdDetails>) detailsResultContext.getResultObject();
                            }

                            sysVpdConfig.setVpdDetails(vpdDetails);
                        });
                        sysVpdConfigs.add(sysVpdConfig);
                        map.put(mapperId, sysVpdConfigs);
                    }
                    map.forEach((k, v) -> {
                        SysVpdAssign assign = new SysVpdAssign();
                        assign.setVpdConfigs(v);
                        setValue(k, assign);
                    });
                }

            });

        } catch (Throwable e) {
            if (logger.isErrorEnabled()) {
                logger.error(e.getMessage(), e);
            }
        }
    }

}
