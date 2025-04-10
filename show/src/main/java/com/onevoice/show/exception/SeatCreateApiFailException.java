package com.onevoice.show.exception;

import com.onevoice.common.dto.ResponseCode;
import com.onevoice.common.exception.BaseException;

public class SeatCreateApiFailException extends BaseException {

    public SeatCreateApiFailException() {
        super(ResponseCode.SESSION_SEAT_CREATE_API_FAIL);
    }
}
