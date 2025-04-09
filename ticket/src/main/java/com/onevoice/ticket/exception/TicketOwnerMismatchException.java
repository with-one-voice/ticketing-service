package com.onevoice.ticket.exception;

import com.onevoice.common.dto.ResponseCode;
import com.onevoice.common.exception.BaseException;

public class TicketOwnerMismatchException extends BaseException {
    public TicketOwnerMismatchException() {
        super(ResponseCode.TICKET_OWNER_MISS__MATCH);
    }
}
