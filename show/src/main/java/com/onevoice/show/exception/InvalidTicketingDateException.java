package com.onevoice.show.exception;

import com.onevoice.common.dto.ResponseCode;
import com.onevoice.common.exception.BaseException;

public class InvalidTicketingDateException extends BaseException {

    public InvalidTicketingDateException() {
        super(ResponseCode.INVALID_TICKETING_DATE);
    }
}
