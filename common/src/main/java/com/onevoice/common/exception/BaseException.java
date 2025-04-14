package com.onevoice.common.exception;

import com.onevoice.common.dto.ResponseCode;

public class BaseException extends RuntimeException {
    private final ResponseCode responseCode;

    public BaseException(ResponseCode responseCode) {
        super(responseCode.getMessage());
        this.responseCode = responseCode;
    }

    public ResponseCode getResponseCode() {
        return responseCode;
    }
}
