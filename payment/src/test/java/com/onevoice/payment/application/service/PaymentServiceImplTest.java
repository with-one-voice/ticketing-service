package com.onevoice.payment.application.service;

import static com.onevoice.payment.fixture.PaymentFixture.createPayment;
import static com.onevoice.payment.fixture.RequestFixture.validRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.onevoice.payment.domain.repository.PaymentRepository;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock
    private PaymentRepository repository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private PaymentServiceImpl service;

    @Test
    @DisplayName("create")
    void testCreate() {
        // given
        var command = validRequest().to(UUID.randomUUID());
        var payment = createPayment(command.to());

        given(repository.save(any())).willReturn(payment);

        // when
        UUID paymentId = service.create(command);

        // then
        assertNotNull(paymentId);
        assertEquals(paymentId, payment.getPaymentId());
    }

//    @Test
//    @DisplayName("삭제: 존재하지 않은 paymentId는 PaymentNotFoundException")
//    void testDelete() {
    // 아직 어떻게 해야 할지 모르겠네... deleteAt..
//        // given
//        var payment = createPayment();
//        assertNull(payment.getDeletedAt());
//        given()
//        // when
//        service.delete(payment.getPaymentId());
//
//        // then
//        assertNotNull(payment.getDeletedAt());
//    }
    // TODO: 나머지 메서드 테스트
}

/* Stub 객체 사용한 테스트
 * private final PaymentServiceImpl service;

    public PaymentServiceImplTest() {
        PaymentRepository repository = new StubPaymentRepository();

        ApplicationEventPublisher publisher = new ApplicationEventPublisher() {
            @Override
            public void publishEvent(Object event) {
            }
        };
        this.service = new PaymentServiceImpl(repository, publisher);
    }

    @Test
    @DisplayName("create")
    void testCreate() {
        // given
        var command = new CreatePaymentCommand(
            UUID.randomUUID(),
            UUID.randomUUID(),
            10000,
            PaymentMethod.CARD
        );

        // when
        UUID paymentId = service.create(command);

        // then
        assertThat(paymentId).isNotNull();
    }
 **/