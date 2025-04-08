package com.onevoice.venue.exception;

import com.onevoice.common.dto.ResponseCode;
import com.onevoice.common.exception.BaseException;

public class DuplicateVenueException extends BaseException {

	public DuplicateVenueException() {
		super(ResponseCode.DUPLICATE_VENUE);
	}
}
