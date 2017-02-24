/*
 * #{copyright}#
 */

package com.lkkhpg.dsis.platform.vpd.mapper;

import com.lkkhpg.dsis.platform.vpd.dto.SysVpdConfig;

/**
 * @author chenjingxiong
 */
public interface SysVpdConfigMapper {
    int deleteByPrimaryKey(Long vpdId);

    int insert(SysVpdConfig record);

    int insertSelective(SysVpdConfig record);

    SysVpdConfig selectByPrimaryKey(Long vpdId);

    int updateByPrimaryKeySelective(SysVpdConfig record);

    int updateByPrimaryKey(SysVpdConfig record);
}