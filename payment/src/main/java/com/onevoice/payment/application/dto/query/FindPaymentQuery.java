package com.onevoice.payment.application.dto.query;

import com.onevoice.payment.domain.Cancellation;
import com.onevoice.payment.domain.Payment;
import com.onevoice.payment.domain.PaymentMethod;
import com.onevoice.payment.domain.PaymentStatus;
import com.onevoice.payment.domain.Refund;
import com.onevoice.payment.domain.RefundStatus;
import java.time.LocalDateTime;
import java.util.UUID;

public record FindPaymentQuery(
    UUID paymentId,
    UUID ticketId,
    UUID userId,
    String pgKey,
    PaymentMethod paymentMethod,
    PaymentStatus paymentStatus,
    int paymentAmount,
    LocalDateTime createdAt,
    FindCancellationQuery cancellationQuery,
    FindRefundQuery refundQuery
) {

    public static FindPaymentQuery from(Payment payment) {
        return new FindPaymentQuery(
            payment.getPaymentId(),
            payment.getTicketId(),
            payment.getUserId(),
            payment.getPgKey(),
            payment.getPaymentMethod(),
            payment.getPaymentStatus(),
            payment.getPaymentAmount(),
            payment.getCreatedAt(),
            payment.getCancellation() != null ? FindCancellationQuery.from(
                payment.getCancellation()) : null,
            payment.getRefund() != null ? FindRefundQuery.from(payment.getRefund()) : null
        );
    }

    public record FindCancellationQuery(
        UUID cancellationId,
        String reason,
        LocalDateTime requestedAt
    ) {

        public static FindCancellationQuery from(Cancellation cancellation) {
            return new FindCancellationQuery(
                cancellation.getCancellationId(),
                cancellation.getReason(),
                cancellation.getRequestedAt()
            );
        }
    }

    public record FindRefundQuery(
        UUID refundId,
        Integer refundAmount,
        String reason,
        RefundStatus refundStatus,
        LocalDateTime requestedAt,
        LocalDateTime completedAt
    ) {

        public static FindRefundQuery from(Refund refund) {
            return new FindRefundQuery(
                refund.getRefundId(),
                refund.getRefundAmount(),
                refund.getReason(),
                refund.getRefundStatus(),
                refund.getRequestedAt(),
                refund.getCompletedAt()
            );
        }

    }
}
