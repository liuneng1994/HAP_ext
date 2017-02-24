/*
 * #{copyright}#
 */

package com.lkkhpg.dsis.platform.vpd;

/**
 * 数据细粒度访问 配置.
 * 
 * @author chenjingxiong on 16/1/5.
 */
public class VpdConfig {

    private String name;

    private String mapper;

    private String sql;

    public VpdConfig(String name, String mapper, String sql) {
        this.name = name;
        this.mapper = mapper;
        this.sql = sql;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMapper() {
        return mapper;
    }

    public void setMapper(String mapper) {
        this.mapper = mapper;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}
