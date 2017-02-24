/*
 * #{copyright}#
 */
package com.lkkhpg.dsis.platform.vpd.sqlsource;

import org.apache.ibatis.builder.annotation.ProviderSqlSource;

import com.github.pagehelper.sqlsource.PageProviderSqlSource;

/**
 * PageProviderSqlSource.
 * @author chenjingxiong
 */
public class VpdPageProviderSqlSource extends PageProviderSqlSource {

    public VpdPageProviderSqlSource(ProviderSqlSource provider) {
        super(provider);
    }

}
