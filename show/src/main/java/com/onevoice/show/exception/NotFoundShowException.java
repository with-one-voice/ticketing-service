package com.onevoice.show.exception;

import com.onevoice.common.dto.ResponseCode;
import com.onevoice.common.exception.BaseException;

public class NotFoundShowException extends BaseException {

    public NotFoundShowException() {
        super(ResponseCode.SHOW_NOT_FOUND);
    }
}
