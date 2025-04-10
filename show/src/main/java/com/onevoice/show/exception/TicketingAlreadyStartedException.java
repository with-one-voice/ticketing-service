package com.onevoice.show.exception;

import com.onevoice.common.dto.ResponseCode;
import com.onevoice.common.exception.BaseException;

public class TicketingAlreadyStartedException extends BaseException {

    public TicketingAlreadyStartedException() {
        super(ResponseCode.TICKETING_ALREADY_STARTED);
    }
}
