package com.onevoice.show.exception;

import com.onevoice.common.dto.ResponseCode;
import com.onevoice.common.exception.BaseException;

public class InvalidVenueIdException extends BaseException {

    public InvalidVenueIdException() {
        super(ResponseCode.INVALID_VENUE_ID);
    }
}
