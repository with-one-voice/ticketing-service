package com.onevoice.user.exception;

import com.onevoice.common.dto.ResponseCode;
import com.onevoice.common.exception.BaseException;

public class DuplicateUserException extends BaseException {

    public DuplicateUserException() {
        super(ResponseCode.DUPLICATE_USER);
    }
}
