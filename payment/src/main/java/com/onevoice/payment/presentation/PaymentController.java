package com.onevoice.payment.presentation;

import com.onevoice.common.dto.CommonResponse;
import com.onevoice.common.dto.ResponseCode;
import com.onevoice.payment.application.dto.query.FindPaymentQuery;
import com.onevoice.payment.application.dto.query.ListPaymentQuery;
import com.onevoice.payment.application.service.PaymentService;
import com.onevoice.payment.presentation.dto.request.CancelPaymentRequest;
import com.onevoice.payment.presentation.dto.request.CreatePaymentRequest;
import com.onevoice.payment.presentation.dto.request.RefundPaymentRequest;
import com.onevoice.payment.presentation.dto.response.CancelPaymentResponse;
import com.onevoice.payment.presentation.dto.response.RefundPaymentResponse;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j(topic = "PaymentController")
@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * 결제 생성 API
     */
    @PostMapping
    public ResponseEntity<?> post(
        @RequestBody @Valid CreatePaymentRequest request,
        @AuthenticationPrincipal UUID userId
    ) {
        log.info("Create payment request: {}, userId: {}", request, userId);
        UUID paymentId = paymentService.create(request.to(userId));
        URI location = UriComponentsBuilder.newInstance()
            .path("/api/payments/{paymentId}")
            .buildAndExpand(paymentId)
            .toUri();
        return CommonResponse.of(ResponseCode.CREATED, location);
    }

    /**
     * 결제 내역 조회 API
     */
    @GetMapping
    public ResponseEntity<?> getList(
        @AuthenticationPrincipal UUID userId,
        Pageable pageable
    ) {
        log.info("Get list: {}, userId: {}", pageable, userId);
        // 자신의 내역만 조회할 수 있다.
        return CommonResponse.success(paymentService.reads(userId, pageable));
    }

    /**
     * 결제 상태 확인 API
     */
    @GetMapping("/{paymentId}")
    public ResponseEntity<?> get(
        @PathVariable UUID paymentId,
        @AuthenticationPrincipal UUID userId
    ) {
        log.info("Get payment request: {}, userId: {}", paymentId, userId);
        return CommonResponse.success(paymentService.read(userId, paymentId));
    }

    /**
     * 결제 취소 API
     */
    @PutMapping("/{paymentId}/cancel")
    public ResponseEntity<?> cancel(
        @PathVariable UUID paymentId,
        @RequestBody @Valid CancelPaymentRequest request,
        @AuthenticationPrincipal UUID userId
    ) {
        log.info("Cancel payment request: {}, userId: {}", paymentId, userId);
        FindPaymentQuery query = paymentService.cancel(paymentId, userId, request.reason());
        CancelPaymentResponse response = CancelPaymentResponse.from(query);
        return CommonResponse.success(response);
    }

    /**
     * 환불 요청 API
     */
    @PutMapping("/{paymentId}/refund")
    public ResponseEntity<?> refund(
        @PathVariable UUID paymentId,
        @RequestBody @Valid RefundPaymentRequest request,
        @AuthenticationPrincipal UUID userId
    ) {
        log.info("Refund payment request: {}, userId: {}", request, userId);
        FindPaymentQuery query = paymentService.refund(paymentId, userId, request.reason());
        RefundPaymentResponse response = RefundPaymentResponse.from(query);
        return CommonResponse.success(response);
    }

    /**
     * 결제 내역 삭제 API
     */
    @Secured("ROLE_ADMIN")
    @DeleteMapping("/{paymentId}")
    public ResponseEntity<?> delete(
        @PathVariable UUID paymentId
    ) {
        log.info("Delete payment request: {}", paymentId);
        paymentService.delete(paymentId);
        return CommonResponse.of(ResponseCode.PAYMENT_NO_CONTENT, "삭제 완료");
    }

    /**
     * 결제 내역 검색 API
     */
    @GetMapping("/search")
    public ResponseEntity<?> search(
        @RequestParam(defaultValue = "PENDING") String keyword,
        @AuthenticationPrincipal UUID userId,
        Pageable pageable
    ) {
        // keyword 는 현재 결제 상태를 검색 합니다.
        log.info("pageSize 검증 10, 30, 50(default 10): {}", pageable.getPageSize());
        ListPaymentQuery queryList = paymentService.search(userId, keyword, pageable);
        return CommonResponse.success(queryList);
    }
}
