package com.onevoice.payment.exception;

import com.onevoice.common.dto.ResponseCode;
import com.onevoice.common.exception.BaseException;

public class PaymentNotStatusException extends BaseException {

    public PaymentNotStatusException() {
        super(ResponseCode.PAYMENT_NOT_STATUS);
    }
}
