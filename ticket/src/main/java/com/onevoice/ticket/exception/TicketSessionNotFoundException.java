package com.onevoice.ticket.exception;

import com.onevoice.common.dto.ResponseCode;
import com.onevoice.common.exception.BaseException;

public class TicketSessionNotFoundException extends BaseException {
    public TicketSessionNotFoundException(){
        super(ResponseCode.TICKET_SESSION_NOT_FOUND);
    }

}
