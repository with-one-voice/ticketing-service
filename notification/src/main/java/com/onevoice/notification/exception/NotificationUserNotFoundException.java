package com.onevoice.notification.exception;

import com.onevoice.common.dto.ResponseCode;
import com.onevoice.common.exception.BaseException;

public class NotificationUserNotFoundException extends BaseException {

    public NotificationUserNotFoundException() {
        super(ResponseCode.NOTIFICATION_USER_NOT_FOUND);
    }
}
