package com.onevoice.notification.fixture;

import com.onevoice.notification.domain.Notification;
import com.onevoice.notification.domain.NotificationType;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.test.util.ReflectionTestUtils;

public class NotificationFixture {

    private static final AtomicInteger counter = new AtomicInteger(1);

    public static UUID nextUUID() {
        long count = counter.getAndIncrement();
        return UUID.nameUUIDFromBytes(String.valueOf(count)
            .getBytes(StandardCharsets.UTF_8));
    }

    public static Notification createNotification() {

        return Notification.create(
            UUID.randomUUID(),
            NotificationType.EMAIL,
            "title",
            "message",
            "metadata"
        );
    }

    public static Notification createNotificationWithId() {
        return createNotificationWithId(createNotification());
    }

    public static Notification createNotificationWithId(Notification notification) {
        ReflectionTestUtils.setField(
            notification,
            "notificationId",
            nextUUID()
        );
        return notification;
    }
}
