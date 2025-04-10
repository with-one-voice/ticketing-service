package com.onevoice.show.exception;

import com.onevoice.common.dto.ResponseCode;
import com.onevoice.common.exception.BaseException;

public class InvalidSessionDateException extends BaseException {

    public InvalidSessionDateException() {
        super(ResponseCode.INVALID_SESSION_DATE);
    }
}
