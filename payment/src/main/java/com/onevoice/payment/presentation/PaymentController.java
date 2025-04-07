package com.onevoice.payment.presentation;

import com.onevoice.payment.application.service.PaymentService;
import com.onevoice.payment.presentation.dto.CreatePaymentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<?> post(
            @RequestBody CreatePaymentRequest request
    ) {
        UUID paymentId = paymentService.create(request.toCommand());
        URI location = UriComponentsBuilder.newInstance()
                .path("/api/payments/{payment_id}")
                .buildAndExpand(paymentId)
                .toUri();
        return ResponseEntity.created(location).build();
    }
}
