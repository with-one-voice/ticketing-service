package com.onevoice.ticket.exception;

import com.onevoice.common.dto.ResponseCode;
import com.onevoice.common.exception.BaseException;

public class TicketNotFoundException extends BaseException {
    public TicketNotFoundException() {
        super(ResponseCode.TICKET_NOT_FOUND);
    }
}
