package com.onevoice.show.exception;

import com.onevoice.common.dto.ResponseCode;
import com.onevoice.common.exception.BaseException;

public class InvalidSeatCountException extends BaseException {

    public InvalidSeatCountException() {
        super(ResponseCode.INVALID_SEAT_COUNT);
    }
}
