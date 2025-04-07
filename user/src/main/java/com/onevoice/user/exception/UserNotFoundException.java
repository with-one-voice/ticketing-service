package com.onevoice.user.exception;

import com.onevoice.common.dto.ResponseCode;
import com.onevoice.common.exception.BaseException;

public class UserNotFoundException extends BaseException {

    public UserNotFoundException(){
        super(ResponseCode.USER_NOT_FOUND);
    }
}
