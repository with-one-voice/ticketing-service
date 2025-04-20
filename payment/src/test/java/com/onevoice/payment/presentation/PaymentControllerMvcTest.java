package com.onevoice.payment.presentation;

import static com.onevoice.payment.fixture.RequestFixture.invalidRequest;
import static com.onevoice.payment.fixture.RequestFixture.validRequest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onevoice.payment.application.dto.command.CreatePaymentCommand;
import com.onevoice.payment.application.service.PaymentService;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

@WebMvcTest(PaymentController.class)
class PaymentControllerMvcTest {

    @Autowired
    private MockMvcTester mvcTester;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PaymentService service;

    @Test
    @DisplayName("결제 생성 요청: 성공 테스트")
    @WithMockUser
    void testCreateSuccess() throws JsonProcessingException {
        // given
        UUID paymentId = UUID.randomUUID();
        given(service.create(any(CreatePaymentCommand.class)))
            .willReturn(paymentId);

        // when & then
        mvcTester.post().uri("/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(validRequest()))
            .with(csrf())
            .assertThat()
            .hasStatus(HttpStatus.CREATED)
            .bodyJson()
            .extractingPath("result")
            .isEqualTo("/api/payments/" + paymentId);
    }

    @Test
    @DisplayName("결제 생성 요청 실패 테스트: 필수 필드 누락")
    @WithMockUser
    void testCreateFailure() throws JsonProcessingException {
        // given
        UUID paymentId = UUID.randomUUID();
        given(service.create(any(CreatePaymentCommand.class)))
            .willReturn(paymentId);

        // when & then
        mvcTester.post().uri("/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(invalidRequest()))
            .with(csrf())
            .assertThat()
            .hasStatus(HttpStatus.BAD_REQUEST)
            .bodyJson()
            .extractingPath("message")
            .isEqualTo("잘못된 요청입니다.");
    }
}