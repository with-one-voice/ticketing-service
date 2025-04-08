package com.onevoice.seat.exception;

import com.onevoice.common.dto.ResponseCode;
import com.onevoice.common.exception.BaseException;

public class SeatNotFoundException extends BaseException {
    public SeatNotFoundException() {
        super(ResponseCode.SEAT_NOT_FOUND);
    }
}
