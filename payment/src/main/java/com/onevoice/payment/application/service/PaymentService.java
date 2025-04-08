package com.onevoice.payment.application.service;

import com.onevoice.payment.application.dto.command.CreatePaymentCommand;
import com.onevoice.payment.application.dto.query.FindPaymentQuery;

import java.util.UUID;

public interface PaymentService {

    UUID create(CreatePaymentCommand command);

    FindPaymentQuery read(UUID paymentId);

    FindPaymentQuery cancel(UUID paymentId, String reason);
}
