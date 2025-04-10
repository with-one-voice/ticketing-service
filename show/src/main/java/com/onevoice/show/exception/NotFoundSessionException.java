package com.onevoice.show.exception;

import com.onevoice.common.dto.ResponseCode;
import com.onevoice.common.exception.BaseException;

public class NotFoundSessionException extends BaseException {

    public NotFoundSessionException() {
        super(ResponseCode.SESSION_NOT_FOUND);
    }
}
