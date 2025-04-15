package com.onevoice.ticket.exception;

import com.onevoice.common.dto.ResponseCode;
import com.onevoice.common.exception.BaseException;

public class DuplicateTicketException extends BaseException {
    public DuplicateTicketException(){
        super(ResponseCode.DUPLICATE_TICKET_EXCEPTION);
    }

}
