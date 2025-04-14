package com.onevoice.notification.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.stream.Stream;

public enum NotificationType {
    EMAIL,
    SMS,
    PUSH,
    ;

    @JsonCreator
    public static NotificationType parsing(String inputValue) {
        return Stream.of(NotificationType.values())
            .filter(type -> type.toString().equals(inputValue.toUpperCase()))
            .findFirst()
            .orElse(null);
    }
}
