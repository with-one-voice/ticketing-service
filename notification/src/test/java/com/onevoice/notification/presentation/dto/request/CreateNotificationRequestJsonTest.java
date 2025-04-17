package com.onevoice.notification.presentation.dto.request;

import static org.assertj.core.api.Assertions.assertThat;

import com.onevoice.notification.domain.NotificationType;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

/**
 * Notification 생성 요청: CreateNotificationRequest 역직렬화 테스트
 */
@JsonTest
class CreateNotificationRequestJsonTest {

    @Autowired
    private JacksonTester<CreateNotificationRequest> json;

    @Test
    @DisplayName("request dto 역직렬화 테스트")
    void deserialize() throws IOException {
        // given
        var content = """
            {
                "notificationType": "EMAIL",
                "title": "title",
                "message": "message",
                "metadata": "metadata"
            }
            """;

        // when & then
        assertThat(json.parse(content))
            .usingRecursiveComparison()
            .isEqualTo(new CreateNotificationRequest(
                NotificationType.EMAIL,
                "title",
                "message",
                "metadata"
            ));
    }

}