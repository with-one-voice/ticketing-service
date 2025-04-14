package com.onevoice.ticket.exception;

import com.onevoice.common.dto.ResponseCode;
import com.onevoice.common.exception.BaseException;

public class RemoteUserNotFoundException extends BaseException {
    public RemoteUserNotFoundException() {
        super(ResponseCode.TICKET_REMOTE_USER_NOT_FOUND);
    }
}
