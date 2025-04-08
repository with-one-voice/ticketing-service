package com.onevoice.seat.exception;
import com.onevoice.common.dto.ResponseCode;
import com.onevoice.common.exception.BaseException;

public class SeatAlreadyHeldException extends BaseException {
    public SeatAlreadyHeldException() {
        super(ResponseCode.SEAT_ALREADY_HELD);
    }
}
