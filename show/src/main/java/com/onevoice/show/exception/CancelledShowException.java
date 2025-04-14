package com.onevoice.show.exception;

import com.onevoice.common.dto.ResponseCode;
import com.onevoice.common.exception.BaseException;

public class CancelledShowException extends BaseException {

    public CancelledShowException() {
        super(ResponseCode.SHOW_CANCELLED);
    }
}
