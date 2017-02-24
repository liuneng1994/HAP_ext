/*
 * #{copyright}#
 */

package com.lkkhpg.dsis.platform.vpd.mapper;

import com.lkkhpg.dsis.platform.vpd.dto.SysVpdDetails;

/**
 * @author chenjingxiong
 */
public interface SysVpdDetailsMapper {
    int deleteByPrimaryKey(Long vpdDetailId);

    int insert(SysVpdDetails record);

    int insertSelective(SysVpdDetails record);

    SysVpdDetails selectByPrimaryKey(Long vpdDetailId);

    int updateByPrimaryKeySelective(SysVpdDetails record);

    int updateByPrimaryKey(SysVpdDetails record);
}