package com.onevoice.venue.exception;

import com.onevoice.common.dto.ResponseCode;
import com.onevoice.common.exception.BaseException;

public class NotFoundVenueException extends BaseException {

    public NotFoundVenueException() {
        super(ResponseCode.VENUE_NOT_FOUND);
    }
}
