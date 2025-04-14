package com.onevoice.notification.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.stream.Stream;

public enum NotificationStatus {
    PENDING,
    SENT,
    FAILED,
    ;

    @JsonCreator
    public static NotificationStatus parsing(String inputValue) {
        return Stream.of(NotificationStatus.values())
            .filter(status -> status.toString().equals(inputValue.toUpperCase()))
            .findFirst()
            .orElse(null);
    }
}
