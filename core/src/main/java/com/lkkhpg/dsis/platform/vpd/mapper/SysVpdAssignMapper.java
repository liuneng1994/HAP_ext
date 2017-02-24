/*
 * #{copyright}#
 */

package com.lkkhpg.dsis.platform.vpd.mapper;

import com.lkkhpg.dsis.platform.vpd.dto.SysVpdAssign;

/**
 * @author chenjingxiong
 */
public interface SysVpdAssignMapper {
    int deleteByPrimaryKey(Long assignId);

    int insert(SysVpdAssign record);

    int insertSelective(SysVpdAssign record);

    SysVpdAssign selectByPrimaryKey(Long assignId);

    int updateByPrimaryKeySelective(SysVpdAssign record);

    int updateByPrimaryKey(SysVpdAssign record);
}