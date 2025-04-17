package com.onevoice.payment.application.dto.query;

import static org.assertj.core.api.Assertions.assertThat;

import com.onevoice.common.dto.CommonResponse;
import com.onevoice.common.dto.ResponseCode;
import com.onevoice.payment.domain.PaymentMethod;
import com.onevoice.payment.domain.PaymentStatus;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

@JsonTest
class FindPaymentQueryJsonTest {

    @Autowired
    private JacksonTester<CommonResponse<FindPaymentQuery>> json;

    @Test
    @DisplayName("response dto 직렬화 테스트")
    void toJson() throws IOException {
        // given
        var query = new FindPaymentQuery(
            UUID.randomUUID(),
            UUID.randomUUID(),
            UUID.randomUUID(),
            "pgKey",
            PaymentMethod.CARD,
            PaymentStatus.COMPLETE,
            10000,
            LocalDateTime.now(),
            null, null
        );

        var response = CommonResponse.createCommonResponse(ResponseCode.SUCCESS, query);

        // when
        var jsonContent = json.write(response);

        // then
        assertThat(jsonContent).extractingJsonPathValue("@.code").isEqualTo(2000);
        assertThat(jsonContent).extractingJsonPathValue("@.message").isEqualTo("요청 성공");

        assertThat(jsonContent).extractingJsonPathMapValue("@.result")
            .containsEntry("pgKey", "pgKey")
            .containsEntry("paymentMethod", PaymentMethod.CARD.name())
            .containsEntry("paymentStatus", PaymentStatus.COMPLETE.name())
            .containsEntry("paymentAmount", 10000);
    }
}