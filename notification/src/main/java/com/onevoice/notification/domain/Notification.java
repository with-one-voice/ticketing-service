package com.onevoice.notification.domain;

import com.onevoice.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "p_notifications")
@NoArgsConstructor
@Getter
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID notificationId;

    private UUID userId;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    @Enumerated(EnumType.STRING)
    private NotificationStatus notificationStatus;

    private String title;

    private String message;

    private String metadata;

    private Notification(
        UUID userId,
        NotificationType notificationType,
        String title,
        String message,
        String metadata
    ) {
        this.userId = userId;
        this.notificationType = notificationType;
        this.title = title;
        this.message = message;
        this.metadata = metadata;

        // 알림 생성 시 status = PENDING
        this.notificationStatus = NotificationStatus.PENDING;
    }

    public static Notification create(
        UUID userId,
        NotificationType notificationType,
        String title,
        String message,
        String metadata
    ) {
        return new Notification(
            userId,
            notificationType,
            title,
            message,
            metadata
        );
    }
}
