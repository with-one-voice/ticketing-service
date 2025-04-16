package com.onevoice.payment.application.service;

import com.onevoice.payment.application.dto.query.KakaoPayReadyQuery;
import java.util.UUID;

public interface KakaoPayService {

    KakaoPayReadyQuery ready(UUID paymentId);

    String approve(UUID paymentId, String pgToken);

    String cancel(UUID paymentId);

    String fail(UUID paymentId);
}