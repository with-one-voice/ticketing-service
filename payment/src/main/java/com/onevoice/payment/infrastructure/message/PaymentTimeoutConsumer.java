package com.onevoice.payment.infrastructure.message;

import com.onevoice.payment.application.event.PaymentTimeoutEvent;
import com.onevoice.payment.application.service.PaymentService;
import com.onevoice.payment.domain.PaymentStatus;
import jakarta.annotation.PostConstruct;
import java.util.concurrent.Executors;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentTimeoutConsumer {

    private final RedissonClient redissonClient;
    private final PaymentService paymentService;

    @PostConstruct
    public void startConsumer() {
        RBlockingQueue<PaymentTimeoutEvent> queue = redissonClient.getBlockingQueue(
            "paymentTimeoutQueue");

        // 단일 인스턴스에서만 큐를 소비함(Payment service 를 여러 개 띄워야 할 경우 메시징 시스템(kafka 등)으로 전환
        Executors.newSingleThreadExecutor().submit(() -> {
            while (true) {
                try {
                    // blocking 방식으로 이벤트 대기
                    PaymentTimeoutEvent event = queue.take();

                    // 실패 처리
                    paymentService.update(event.paymentId(), PaymentStatus.PG_FAIL);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        });
    }
}
