/*
 * #{copyright}#
 */
package com.lkkhpg.dsis.platform.vpd.exception;


import com.hand.hap.core.exception.BaseException;

/**
 * VPD解析异常.
 * @author chenjingxiong
 */
public class VpdParseErrorException extends BaseException {
    
    private static final long serialVersionUID = 454180848490470615L;
    
    private static final String MSG_ERROR_VPD_PARSE_ERROR = "msg.error.vpd.parse_error";
    
    public VpdParseErrorException() {
        super(MSG_ERROR_VPD_PARSE_ERROR, MSG_ERROR_VPD_PARSE_ERROR,  new Object[0]);
    }

    public VpdParseErrorException(String expression) {
        super(MSG_ERROR_VPD_PARSE_ERROR, MSG_ERROR_VPD_PARSE_ERROR,  new Object[] {expression});
    }

}
