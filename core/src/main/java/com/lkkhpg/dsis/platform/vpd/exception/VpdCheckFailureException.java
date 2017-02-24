/*
 * #{copyright}#
 */
package com.lkkhpg.dsis.platform.vpd.exception;

import com.hand.hap.core.exception.BaseException;

/**
 * vpd校验未通过，则抛出该异常.
 * 
 * @author chenjingxiong
 */
public class VpdCheckFailureException extends BaseException {

    private static final long serialVersionUID = -8451118078039399473L;

    private static final String MSG_ERROR_VPD_CHECK_FAILURE = "msg.error.vpd.check_failure";

    public VpdCheckFailureException() {
        super(MSG_ERROR_VPD_CHECK_FAILURE, MSG_ERROR_VPD_CHECK_FAILURE, new Object[0]);
    }
}
