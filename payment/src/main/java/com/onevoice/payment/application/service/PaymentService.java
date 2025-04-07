package com.onevoice.payment.application.service;

import com.onevoice.payment.application.dto.CreatePaymentCommand;

import java.util.UUID;

public interface PaymentService {

    UUID create(CreatePaymentCommand command);
}
