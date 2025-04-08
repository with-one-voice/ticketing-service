package com.onevoice.payment.application.service;

import com.onevoice.payment.application.dto.command.CreatePaymentCommand;
import com.onevoice.payment.application.dto.query.FindPaymentQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface PaymentService {

    UUID create(CreatePaymentCommand command);

    Page<FindPaymentQuery> reads(Pageable pageable);

    FindPaymentQuery read(UUID paymentId);

    FindPaymentQuery cancel(UUID paymentId, String reason);

    FindPaymentQuery refund(UUID paymentId, String reason);
}
