package com.onevoice.payment.presentation.dto.request;

import static org.assertj.core.api.Assertions.assertThat;

import com.onevoice.payment.domain.PaymentMethod;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CreatePaymentRequestValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    @DisplayName("유효한 값으로 요청")
    void testValid() {
        // given
        var payment = new CreatePaymentRequest(
            UUID.randomUUID(),
            10000,
            PaymentMethod.CARD
        );

        // when
        Set<ConstraintViolation<CreatePaymentRequest>> violations = validator.validate(payment);

        // then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("유효하지 않은 값으로 요청")
    void testInvalid() {
        // given
        var payment = new CreatePaymentRequest(
            null,
            10000,
            PaymentMethod.CARD
        );

        // when
        Set<ConstraintViolation<CreatePaymentRequest>> violations = validator.validate(payment);

        // then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
            .isEqualTo("티켓 정보가 필요합니다.");
    }
}