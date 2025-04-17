package com.onevoice.payment.application.service;

import com.onevoice.common.enumtype.KafkaTopicType;
import com.onevoice.payment.application.dto.command.CreatePaymentCommand;
import com.onevoice.payment.application.dto.message.PaymentCreateMessage;
import com.onevoice.payment.application.dto.message.PaymentFailMessage;
import com.onevoice.payment.application.dto.message.PaymentSuccessMessage;
import com.onevoice.payment.application.dto.query.FindPaymentQuery;
import com.onevoice.payment.application.dto.query.ListPaymentQuery;
import com.onevoice.payment.application.event.GenericKafkaEvent;
import com.onevoice.payment.domain.Payment;
import com.onevoice.payment.domain.PaymentStatus;
import com.onevoice.payment.domain.repository.PaymentRepository;
import com.onevoice.payment.exception.PaymentNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository repository;
    private final ApplicationEventPublisher eventPublisher;

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

        KafkaTopicType topicType = KafkaTopicType.PAYMENT_CREATE;
        PaymentCreateMessage payload = new PaymentCreateMessage(command.ticketId());
        eventPublish(topicType, payload);
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
    public FindPaymentQuery read(UUID paymentId) {
        return repository.findById(paymentId)
            .map(FindPaymentQuery::from)
            .orElseThrow(PaymentNotFoundException::new);
    }

    @Override
    @Transactional(readOnly = true)
    public FindPaymentQuery read(UUID userId, UUID paymentId) {
        return repository.findByIdAndUserId(paymentId, userId)
            .map(FindPaymentQuery::from)
            .orElseThrow(PaymentNotFoundException::new);
    }

    @Override
    public void update(UUID paymentId, PaymentStatus paymentStatus) {
        log.info("Updating payment {}", paymentId);
        Payment payment = repository.findById(paymentId)
            .orElseThrow(PaymentNotFoundException::new);
        payment.update(paymentStatus);

        // 결제 결과 이벤트 발행
        KafkaTopicType topicType;
        if (Objects.requireNonNull(paymentStatus) == PaymentStatus.PG_APPROVE) {
            PaymentSuccessMessage payload = new PaymentSuccessMessage(payment.getTicketId());
            topicType = KafkaTopicType.PAYMENT_SUCCESS;
            eventPublish(topicType, payload);
        } else {
            topicType = KafkaTopicType.PAYMENT_FAIL;
            PaymentFailMessage payload = new PaymentFailMessage(payment.getTicketId());
            eventPublish(topicType, payload);
        }
    }

    @Override
    public void update(UUID paymentId, String pgKey, PaymentStatus paymentStatus) {
        Payment payment = repository.findById(paymentId)
            .orElseThrow(PaymentNotFoundException::new);
        payment.update(pgKey, paymentStatus);
    }

    @Override
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

    private <T> void eventPublish(KafkaTopicType topicType, T payload) {
        GenericKafkaEvent<T> event = new GenericKafkaEvent<>(
            topicType.getTopic(),
            payload);
        eventPublisher.publishEvent(event);
    }
}
