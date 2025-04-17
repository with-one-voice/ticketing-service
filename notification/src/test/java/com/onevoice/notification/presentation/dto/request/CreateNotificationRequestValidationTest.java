package com.onevoice.notification.presentation.dto.request;

import static org.assertj.core.api.Assertions.assertThat;

import com.onevoice.notification.domain.NotificationType;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Notification 생성 요청: CreateNotificationRequest 요청값 검증 테스트
 */
class CreateNotificationRequestValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    @DisplayName("유효한 값으로 요청")
    void validate() {
        // given
        var notification = new CreateNotificationRequest(
            NotificationType.EMAIL,
            "title",
            "message",
            "metadata"
        );

        // when
        Set<ConstraintViolation<CreateNotificationRequest>> violations = validator.validate(
            notification);

        // then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("유효하지 않은 값으로 요청")
    void validateNotificationType() {
        var notification = new CreateNotificationRequest(
            null,
            "title",
            "",
            "metadata"
        );

        // when
        Set<ConstraintViolation<CreateNotificationRequest>> violations = validator.validate(
            notification);

        // then
        assertThat(violations).hasSize(2);
        List<String> messages = violations.stream()
            .map(ConstraintViolation::getMessage)
            .collect(Collectors.toList());
        assertThat(messages)
            .containsExactlyInAnyOrder(
                "메시지 내용을 입력해 주세요.",
                "메시지 타입을 올바르게 지정해 주세요. EMAIL | SMS | PUSH"
            );
    }
}