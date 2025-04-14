package com.onevoice.notification.exception;

import com.onevoice.common.dto.ResponseCode;
import com.onevoice.common.exception.BaseException;

public class NotificationNotFoundException extends BaseException {

    public NotificationNotFoundException() {
        super(ResponseCode.NOTIFICATION_NOT_FOUND);
    }
}
