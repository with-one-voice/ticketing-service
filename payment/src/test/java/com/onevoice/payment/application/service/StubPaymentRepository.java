package com.onevoice.payment.application.service;

import static com.onevoice.payment.fixture.PaymentFixture.createPayment;

import com.onevoice.payment.domain.Payment;
import com.onevoice.payment.domain.repository.PaymentRepository;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class StubPaymentRepository implements PaymentRepository {

    private static final Logger log = LoggerFactory.getLogger(StubPaymentRepository.class);
    private final Map<UUID, Payment> paymentMap = new HashMap<>();

    @Override
    public Payment save(Payment payment) {
        Payment save = createPayment(payment);
        paymentMap.put(save.getPaymentId(), save);
        return save;
    }

    @Override
    public Page<Payment> findAllByUserId(UUID userId, Pageable pageable) {
        List<Payment> paymentList = paymentMap.values().stream()
            .filter(payment -> payment.getUserId().equals(userId))
            .toList();

        if (pageable.getSort().isSorted()) {
            Comparator<Payment> comparator = pageable.getSort().stream()
                .map(sort -> {
                    Comparator<Payment> paymentComparator;
                    if (sort.getProperty().equalsIgnoreCase("paymentMethod")) {
                        paymentComparator = Comparator.comparing(Payment::getPaymentMethod);
                    } else if (sort.getProperty().equalsIgnoreCase("paymentState")) {
                        paymentComparator = Comparator.comparing(Payment::getPaymentStatus);
                    } else {
                        paymentComparator = Comparator.comparing(Payment::getPaymentAmount);
                    }
                    return sort.isAscending() ? paymentComparator : paymentComparator.reversed();
                })
                .reduce(Comparator::thenComparing)
                .orElse(Comparator.comparing(Payment::getPaymentId)); // 기본정렬값
            paymentList = paymentList.stream()
                .sorted(comparator)
                .toList();
        }

        // 페이징 처리
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), paymentList.size());
        List<Payment> pageContent = (start > paymentList.size()) ?
            Collections.emptyList() : paymentList.subList(start, end);
        return new PageImpl<>(pageContent, pageable, paymentList.size());
    }

    @Override
    public Optional<Payment> findByPaymentIdAndUserId(UUID paymentId, UUID userId) {
        return paymentMap.values().stream()
            .filter(payment -> payment.getUserId().equals(userId))
            .filter(payment -> payment.getPaymentId().equals(paymentId))
            .findAny();
    }

    @Override
    public Optional<Payment> findByPaymentId(UUID paymentId) {
        return paymentMap.values().stream()
            .filter(payment -> payment.getPaymentId().equals(paymentId))
            .findAny();
    }

    @Override
    public Page<Payment> retrieve(UUID userId, String keyword, Pageable pageable) {
        return null;
    }
}
