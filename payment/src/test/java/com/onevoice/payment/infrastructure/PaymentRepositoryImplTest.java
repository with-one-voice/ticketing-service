package com.onevoice.payment.infrastructure;

import static com.onevoice.payment.fixture.PaymentFixture.createPayment;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.onevoice.payment.domain.Payment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@Import({PaymentRepositoryImpl.class, QueryDslTestConfig.class})
@DataJpaTest
class PaymentRepositoryImplTest {

    @Autowired
    PaymentRepositoryImpl repository;

    @Test
    void test() {
        // 설정 확인
    }

    @Test
    @DisplayName("저장")
    void testSave() {
        // given
        Payment payment = createPayment();

        // when
        Payment saved = repository.save(payment);

        // then
        assertNotNull(saved);
        assertEquals(saved.getPaymentId(), payment.getPaymentId());
    }

    // TODO: 나머지 메서드 테스트
}