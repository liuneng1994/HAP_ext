/*
 * #{copyright}#
 */
package com.lkkhpg.dsis.platform.vpd.parser;

import net.sf.jsqlparser.JSQLParserException;

import com.lkkhpg.dsis.platform.vpd.exception.VpdParseErrorException;

/**
 * VPD解析器.
 * @author chenjingxiong
 */
public interface IVpdParser {
    
    boolean canParse(String mapperId);
    
    String parseAndBuild(String mapperId, String sql) throws JSQLParserException, VpdParseErrorException;
}
