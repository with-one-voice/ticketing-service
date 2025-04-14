package com.onevoice.payment.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.stream.Stream;

public enum PaymentMethod {
    CARD,
    ACCOUNT,
    ;

    @JsonCreator
    public static PaymentMethod parsing(String inputValue) {
        return Stream.of(PaymentMethod.values())
            .filter(method -> method.toString().equals(inputValue.toUpperCase()))
            .findFirst()
            .orElse(null);
    }
}
