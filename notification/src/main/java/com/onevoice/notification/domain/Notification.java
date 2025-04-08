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
    private UUID notification_id;

    private UUID user_id;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    private String message;

    @Enumerated(EnumType.STRING)
    private NotificationStatus status;

    private Notification(
        UUID user_id,
        NotificationType type,
        String message,
        NotificationStatus status
    ) {
        this.user_id = user_id;
        this.type = type;
        this.message = message;
        this.status = status;
    }

    public static Notification create(
        UUID user_id,
        NotificationType type,
        String message,
        NotificationStatus status
    ) {
        return new Notification(
            user_id,
            type,
            message,
            status
        );
    }
}
