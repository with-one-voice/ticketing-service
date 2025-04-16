package com.onevoice.payment.application.service;

import com.onevoice.payment.application.dto.command.KakaoPayApproveCommand;
import com.onevoice.payment.application.dto.command.KakaoPayReadyCommand;
import com.onevoice.payment.application.dto.query.FindPaymentQuery;
import com.onevoice.payment.application.dto.query.KakaoPayReadyQuery;
import com.onevoice.payment.domain.PaymentStatus;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoPayServiceImpl implements KakaoPayService {

    private final PaymentService paymentService;

    @Value("${kakaopay.api.secret.key}")
    private String kakaoPaySecretKey;

    @Value("${kakaopay.cid}")
    private String cid;

    @Value("${kakaopay.host}")
    private String host;

    @Override
    public KakaoPayReadyQuery ready(UUID paymentId) {
        // Request header
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "DEV_SECRET_KEY " + kakaoPaySecretKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        FindPaymentQuery paymentQuery = paymentService.read(paymentId);
        if (!paymentQuery.paymentMethod().name().equals("CARD")) {
            throw new RuntimeException("Invalid payment method");
        }

        // Request param: 필수 값만 지정
        KakaoPayReadyCommand readyCommand = KakaoPayReadyCommand.builder()
            .cid(cid)
            .partnerOrderId("1")
            .partnerUserId("1")
            .itemName("상품명")
            .quantity(1)
            .totalAmount(1100)
            .taxFreeAmount(0)
            .vatAmount(100)
            .approvalUrl(host + "/kakao/approve/" + paymentId)
            .cancelUrl(host + "/kakao/cancel/" + paymentId)
            .failUrl(host + "/kakao/fail/" + paymentId)
            .build();

        log.info("Payment ready: {}", readyCommand);

        // Send request
        HttpEntity<KakaoPayReadyCommand> entityMap = new HttpEntity<>(readyCommand, headers);
        ResponseEntity<KakaoPayReadyQuery> response = new RestTemplate().postForEntity(
            "https://open-api.kakaopay.com/online/v1/payment/ready",
            entityMap,
            KakaoPayReadyQuery.class
        );

        KakaoPayReadyQuery readyQuery = response.getBody();

        // 주문번호와 TID(pgKey) 를 매핑해서 저장해 놓는다.
        paymentService.update(paymentId, readyQuery.getTid(), PaymentStatus.PG_READY);
        return readyQuery;
    }

    @Override
    public String approve(UUID paymentId, String pgToken) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "DEV_SECRET_KEY " + kakaoPaySecretKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // ready 할 때 저장해놓은 TID(pgKey)로 승인 요청
        FindPaymentQuery paymentQuery = paymentService.read(paymentId);
        String tid = paymentQuery.pgKey();

        // Request param
        KakaoPayApproveCommand approveCommand = KakaoPayApproveCommand.builder()
            .cid(cid)
            .tid(tid)
            .partnerOrderId("1")
            .partnerUserId("1")
            .pgToken(pgToken)
            .build();

        // Send request
        HttpEntity<KakaoPayApproveCommand> entityMap = new HttpEntity<>(approveCommand, headers);
        try {
            ResponseEntity<String> response = new RestTemplate().postForEntity(
                "https://open-api.kakaopay.com/online/v1/payment/approve",
                entityMap,
                String.class
            );

            String approveResponse = response.getBody();
            // TODO: response 에서 카드 결제가 성공했는지 실패했는지 확인해야 한다.
            // 참고 https://developers.kakaopay.com/docs/payment/online/single-payment
            paymentService.update(paymentId, PaymentStatus.PG_APPROVE);
            return approveResponse;
        } catch (HttpStatusCodeException ex) {
            return ex.getResponseBodyAsString();
        }
    }

    @Override
    public String cancel(UUID paymentId) {
        // 결제내역조회(/v1/payment/status) api 에서 status 를 확인한다.
        paymentService.update(paymentId, PaymentStatus.PG_CANCEL);
        return "결제 취소";
    }

    @Override
    public String fail(UUID paymentId) {
        // 결제내역조회(/v1/payment/status) api 에서 status 를 확인한다.
        paymentService.update(paymentId, PaymentStatus.PG_FAIL);
        return "결제 실패";
    }
}
