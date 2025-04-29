package com.onevoice.payment.presentation;

import com.onevoice.payment.application.dto.query.KakaoPayReadyQuery;
import com.onevoice.payment.application.service.KakaoPayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/rest/kakao")
@RequiredArgsConstructor
public class KakkoRestContoller {

    private final KakaoPayService kakaoPayService;

    @GetMapping("/ready/{paymentId}")
    public ResponseEntity<KakaoPayReadyQuery> ready(@PathVariable UUID paymentId) {
        KakaoPayReadyQuery ready = kakaoPayService.ready(paymentId);
        return ResponseEntity.ok(ready); // 💡 JSON 반환
    }

    @GetMapping("/approve/{paymentId}")
    public ResponseEntity<String> approve(
            @PathVariable UUID paymentId,
            @RequestParam("pg_token") String pgToken
    ) {
        String response = kakaoPayService.approve(paymentId, pgToken);
        return ResponseEntity.ok(response);
    }

}
