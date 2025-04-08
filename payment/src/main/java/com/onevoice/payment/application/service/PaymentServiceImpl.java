package com.onevoice.payment.application.service;

import com.onevoice.payment.application.dto.command.CreatePaymentCommand;
import com.onevoice.payment.application.dto.query.FindPaymentQuery;
import com.onevoice.payment.domain.Payment;
import com.onevoice.payment.domain.repository.PaymentRepository;
import com.onevoice.payment.exception.PaymentNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Override
    public UUID create(CreatePaymentCommand command) {

        // 엔티티에 객체 생성에 대한 책임을 부여한다.
        Payment payment = Payment.createPayment(
                command.ticketId(),
                command.userId(),
                command.methodType(),
                command.amount()
        );
        Payment saved = paymentRepository.save(payment);
        return saved.getPaymentId();
    }

    @Override
    public FindPaymentQuery read(UUID paymentId) {
        return paymentRepository.findById(paymentId)
                .map(FindPaymentQuery::from)
                .orElseThrow(PaymentNotFoundException::new);
    }

    @Override
    public FindPaymentQuery cancel(UUID paymentId, String reason) {
        // 요청된 결제 정보 가져오기
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(PaymentNotFoundException::new);

        // 애그리거트 루트에 취소 처리 책임을 부여
        payment.cancel(reason);

        Payment cancelled = paymentRepository.save(payment);
        return null;
    }
}
