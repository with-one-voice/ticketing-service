package com.onevoice.payment.application.service;

import com.onevoice.payment.application.dto.command.CreatePaymentCommand;
import com.onevoice.payment.application.dto.query.FindPaymentQuery;
import com.onevoice.payment.application.dto.query.ListPaymentQuery;
import com.onevoice.payment.domain.Payment;
import com.onevoice.payment.domain.repository.PaymentRepository;
import com.onevoice.payment.exception.PaymentNotFoundException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository repository;

    @Override
    public UUID create(CreatePaymentCommand command) {

        // 엔티티에 객체 생성에 대한 책임을 부여한다.
        Payment payment = Payment.createPayment(
            command.ticketId(),
            command.userId(),
            command.paymentMethod(),
            command.paymentAmount()
        );
        Payment saved = repository.save(payment);
        return saved.getPaymentId();
    }

    @Override
    @Transactional(readOnly = true)
    public ListPaymentQuery reads(UUID userid, Pageable pageable) {
        // 실제 검색은 deletedAt is Null 포함
        List<FindPaymentQuery> queryList = repository.findAllByUserId(userid, pageable).stream()
            .map(FindPaymentQuery::from)
            .toList();
        return new ListPaymentQuery(queryList);
    }

    @Override
    @Transactional(readOnly = true)
    public FindPaymentQuery read(UUID userId, UUID paymentId) {
        return repository.findByIdAndUserId(paymentId, userId)
            .map(FindPaymentQuery::from)
            .orElseThrow(PaymentNotFoundException::new);
    }

    @Override
    @Transactional
    public FindPaymentQuery cancel(
        UUID paymentId,
        UUID userId,
        String reason
    ) {
        // 요청된 결제 정보 가져오기
        Payment payment = repository.findByIdAndUserId(paymentId, userId)
            .orElseThrow(PaymentNotFoundException::new);

        // 애그리거트 루트에 취소 처리 책임을 부여
        payment.cancel(reason);

        Payment cancelled = repository.save(payment);
        return FindPaymentQuery.from(cancelled);
    }

    @Override
    @Transactional
    public FindPaymentQuery refund(
        UUID paymentId,
        UUID userId,
        String reason
    ) {
        // 요청된 결제 정보 가져오기
        Payment payment = repository.findByIdAndUserId(paymentId, userId)
            .orElseThrow(PaymentNotFoundException::new);

        payment.refund(reason);

        Payment refunded = repository.save(payment);
        return FindPaymentQuery.from(refunded);
    }

    @Override
    @Transactional
    public void delete(UUID paymentId) {
        Payment payment = repository.findById(paymentId)
            .orElseThrow(PaymentNotFoundException::new);
        payment.delete(paymentId);
    }

    @Override
    @Transactional(readOnly = true)
    public ListPaymentQuery search(
        UUID userId,
        String keyword,
        Pageable pageable
    ) {
        List<FindPaymentQuery> queryList = repository.retrieve(userId, keyword, pageable)
            .map(FindPaymentQuery::from)
            .toList();
        return new ListPaymentQuery(queryList);
    }
}
