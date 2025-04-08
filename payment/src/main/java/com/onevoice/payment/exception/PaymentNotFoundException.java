package com.onevoice.payment.exception;

import com.onevoice.common.dto.ResponseCode;
import com.onevoice.common.exception.BaseException;

public class PaymentNotFoundException extends BaseException {

    public PaymentNotFoundException() {
        super(ResponseCode.PAYMENT_NOT_FOUND);
    }
}
