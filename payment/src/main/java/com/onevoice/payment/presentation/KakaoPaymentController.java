package com.onevoice.payment.presentation;

import com.onevoice.payment.application.dto.query.KakaoPayReadyQuery;
import com.onevoice.payment.application.service.KakaoPayService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequestMapping("/kakao")
@RequiredArgsConstructor
public class KakaoPaymentController {

    private final KakaoPayService kakaoPayService;

    @GetMapping
    public String index() {
        log.info("Kakao payment index");
        return "index";
    }

    @GetMapping("/ready/{paymentId}")
    public String ready(@PathVariable UUID paymentId, Model model) {
        log.info("Kakao payment ready");
        KakaoPayReadyQuery ready = kakaoPayService.ready(paymentId);
        model.addAttribute("response", ready);
        model.addAttribute("paymentId", paymentId);
        return "ready";
    }

    @GetMapping("/approve/{paymentId}")
    public String approve(
        @PathVariable UUID paymentId,
        @RequestParam("pg_token") String pgToken,
        Model model
    ) {
        log.info("Kakao payment approve");
        String approveResponse = kakaoPayService.approve(paymentId, pgToken);
        log.info("Approve response: {}", approveResponse);
        model.addAttribute("response", approveResponse);
        model.addAttribute("paymentId", paymentId);
        return "approve";
    }

    @GetMapping("/cancel/{paymentId}")
    public String cancel(@PathVariable UUID paymentId, Model model) {
        // 주문건이 진짜 취소되었는지 확인 후 취소 처리
        // 결제내역조회(/v1/payment/status) api 에서 status 를 확인한다.
        String cancelResponse = kakaoPayService.cancel(paymentId);
        model.addAttribute("response", cancelResponse);
        return "cancel";
    }

    @GetMapping("/fail/{paymentId}")
    public String fail(@PathVariable UUID paymentId, Model model) {
        // 주문건이 진짜 실패되었는지 확인 후 실패 처리
        // 결제내역조회(/v1/payment/status) api 에서 status 를 확인한다.
        String failResponse = kakaoPayService.fail(paymentId);
        model.addAttribute("response", failResponse);
        return "fail";
    }
}
