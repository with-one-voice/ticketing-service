package com.onevoice.show.exception;

import com.onevoice.common.dto.ResponseCode;
import com.onevoice.common.exception.BaseException;

public class DuplicateShowException extends BaseException {

    public DuplicateShowException() {
        super(ResponseCode.DUPLICATE_SHOW);
    }
}
