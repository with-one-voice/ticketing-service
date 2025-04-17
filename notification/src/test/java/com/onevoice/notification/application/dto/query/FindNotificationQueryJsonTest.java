package com.onevoice.notification.application.dto.query;

import static org.assertj.core.api.Assertions.assertThat;

import com.onevoice.common.dto.CommonResponse;
import com.onevoice.common.dto.ResponseCode;
import com.onevoice.notification.domain.NotificationStatus;
import com.onevoice.notification.domain.NotificationType;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

@JsonTest
class FindNotificationQueryJsonTest {

    private static final Logger log = LoggerFactory.getLogger(FindNotificationQueryJsonTest.class);
    @Autowired
    private JacksonTester<CommonResponse<FindNotificationQuery>> json;

    @Test
    @DisplayName("응답 객체 직렬화 테스트")
    void toJson() throws IOException {
        // given
        var query = new FindNotificationQuery(
            UUID.randomUUID(),
            NotificationType.EMAIL,
            NotificationStatus.PENDING,
            "title", "message", "metadata",
            LocalDateTime.now()
        );
        var response = CommonResponse.createCommonResponse(ResponseCode.SUCCESS, query);

        // when
        var jsonContent = json.write(response);

        log.info(jsonContent.toString());
        //then
        assertThat(jsonContent).extractingJsonPathValue("@.code").isEqualTo(2000);
        assertThat(jsonContent).extractingJsonPathValue("@.message").isEqualTo("요청 성공");

        assertThat(jsonContent).extractingJsonPathMapValue("@.result")
            .containsEntry("notificationType", NotificationType.EMAIL.name())
            .containsEntry("title", "title");

    }
}