package com.onevoice.payment.presentation.dto.request;

import static org.assertj.core.api.Assertions.assertThat;

import com.onevoice.payment.domain.PaymentMethod;
import java.io.IOException;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

@JsonTest
class CreatePaymentRequestJsonTest {

    @Autowired
    private JacksonTester<CreatePaymentRequest> json;

    @Test
    @DisplayName("request dto 역직렬화 테스트")
    void deserialize() throws IOException {
        // given
        var content = """
            {
                "ticketId": "986817d9-fab6-48bd-bb26-998864416a5c",
                "paymentAmount": 10000,
                "paymentMethod": "CARD"
            }
            """;

        // when & then
        assertThat(json.parse(content))
            .usingRecursiveComparison()
            .isEqualTo(new CreatePaymentRequest(
                UUID.fromString("986817d9-fab6-48bd-bb26-998864416a5c"),
                10000,
                PaymentMethod.CARD
            ));
    }
}