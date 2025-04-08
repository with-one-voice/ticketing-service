package com.onevoice.payment.presentation;

import com.onevoice.common.dto.CommonResponse;
import com.onevoice.common.dto.ResponseCode;
import com.onevoice.payment.application.dto.query.FindPaymentQuery;
import com.onevoice.payment.application.service.PaymentService;
import com.onevoice.payment.presentation.dto.request.CancelPaymentRequest;
import com.onevoice.payment.presentation.dto.request.CreatePaymentRequest;
import com.onevoice.payment.presentation.dto.request.RefundPaymentRequest;
import com.onevoice.payment.presentation.dto.response.CancelPaymentResponse;
import com.onevoice.payment.presentation.dto.response.RefundPaymentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@Slf4j(topic = "PaymentController")
@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    // TODO: security 에서 userId를 받아와야 한다.

    /**
     * 결제 생성 API
     */
    @PostMapping
    public ResponseEntity<?> post(
            @RequestBody CreatePaymentRequest request
    ) {
        log.info("Create payment request: {}", request);
        UUID paymentId = paymentService.create(request.toCommand(UUID.randomUUID()));
        URI location = UriComponentsBuilder.newInstance()
                .path("/api/payments/{payment_id}")
                .buildAndExpand(paymentId)
                .toUri();
        return CommonResponse.of(ResponseCode.CREATED, location);
    }

    /**
     * 결제 내역 조회 API
     */
    @GetMapping
    public ResponseEntity<?> getList(
            Pageable pageable
    ) {
        log.info("Get list");
        // TODO: 페이징 처리
        return CommonResponse.success(paymentService.reads(pageable));
    }

    /**
     * 결제 상태 확인 API
     */
    @GetMapping("/{paymentId}")
    public ResponseEntity<?> get(
            @PathVariable UUID paymentId
    ) {
        log.info("Get payment request: {}", paymentId);
        return CommonResponse.success(paymentService.read(paymentId));
    }

    /**
     * 결제 취소 API
     */
    @PutMapping("/{paymentId}/cancel")
    public ResponseEntity<?> cancel(
            @PathVariable UUID paymentId,
            @RequestBody CancelPaymentRequest request
    ) {
        log.info("Cancel payment request: {}", paymentId);
        FindPaymentQuery cancel = paymentService.cancel(paymentId, request.reason());
        CancelPaymentResponse response = CancelPaymentResponse.from(cancel);
        return CommonResponse.success(response);
    }

    /**
     * 환불 요청 API
     */
    @PutMapping("/{paymentId}/refund")
    public ResponseEntity<?> refund(
            @PathVariable UUID paymentId,
            @RequestBody RefundPaymentRequest request
    ) {
        log.info("Refund payment request: {}", request);
        FindPaymentQuery refund = paymentService.refund(paymentId, request.reason());
        RefundPaymentResponse response = RefundPaymentResponse.from(refund);
        return CommonResponse.success(response);
    }
}
