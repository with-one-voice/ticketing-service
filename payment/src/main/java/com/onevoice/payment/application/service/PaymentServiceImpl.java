package com.onevoice.payment.application.service;

import com.onevoice.payment.application.dto.CreatePaymentCommand;
import com.onevoice.payment.domain.Payment;
import com.onevoice.payment.domain.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Override
    @Transactional
    public UUID create(CreatePaymentCommand command) {
        Payment payment = Payment.createPayment(
                command.ticketId(),
                UUID.randomUUID(),
                command.methodType(),
                command.amount()
        );
        paymentRepository.save(payment);
        return payment.getPaymentId();
    }
}
