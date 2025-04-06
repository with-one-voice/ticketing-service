package com.onevoice.user.exception;

import com.onevoice.common.dto.ResponseCode;
import com.onevoice.common.exception.BaseException;

public class PasswordNotMatchException extends BaseException {

    public PasswordNotMatchException(){
        super(ResponseCode.PASSWORD_NOT_MATCH);
    }

}
