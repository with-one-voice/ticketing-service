package com.onevoice.show.exception;

import com.onevoice.common.dto.ResponseCode;
import com.onevoice.common.exception.BaseException;

public class DuplicateSessionException extends BaseException {

    public DuplicateSessionException() {
        super(ResponseCode.DUPLICATE_SESSION);
    }
}
