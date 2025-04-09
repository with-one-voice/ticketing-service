package com.onevoice.payment.application.service;

import com.onevoice.payment.application.dto.command.CreatePaymentCommand;
import com.onevoice.payment.application.dto.query.FindPaymentQuery;
import com.onevoice.payment.application.dto.query.ListPaymentQuery;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface PaymentService {

    UUID create(CreatePaymentCommand command);

    ListPaymentQuery reads(UUID userId, Pageable pageable);

    FindPaymentQuery read(UUID userId, UUID paymentId);

    FindPaymentQuery cancel(UUID paymentId, UUID userId, String reason);

    FindPaymentQuery refund(UUID paymentId, UUID userId, String reason);

    ListPaymentQuery search(UUID userId, String keyword, Pageable pageable);

    void delete(UUID paymentId);
}
