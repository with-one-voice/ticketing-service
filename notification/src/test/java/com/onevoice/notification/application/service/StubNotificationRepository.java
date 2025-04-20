package com.onevoice.notification.application.service;

import static com.onevoice.notification.fixture.NotificationFixture.createNotificationWithId;

import com.onevoice.notification.domain.Notification;
import com.onevoice.notification.domain.repository.NotificationRepository;
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

/**
 * Service 를 테스트하기 위한 Stub
 */
public class StubNotificationRepository implements NotificationRepository {

    private static final Logger log = LoggerFactory.getLogger(StubNotificationRepository.class);
    private final Map<UUID, Notification> notificationMap = new HashMap<>();

    @Override
    public Notification save(Notification notification) {
        Notification save = createNotificationWithId(notification);
        notificationMap.put(save.getNotificationId(), save);
        // map 은 key 가 처음 저장되는 거면 put() 메소드는 null 을 반환
        // 기존 key 에 저장되어 있는 값이 있었으면 put() 은 저장하는 값을 저장하고 기존 key 에 저장되어 있던 값을 리턴한다.
        return save;
    }

    @Override
    public Page<Notification> findAllByUserId(UUID userId, Pageable pageable) {
        // map to list
        List<Notification> notificationList = notificationMap.values().stream()
            .filter(notification -> notification.getUserId().equals(userId))
            .toList();

        // 정렬 처리를 위한 Comparator
        if (pageable.getSort().isSorted()) {
            Comparator<Notification> comparator = pageable.getSort().stream()
                .map(sort -> {
                    Comparator<Notification> singleComparator = Comparator.comparing(
                        notification -> {
                            if (sort.getProperty().equalsIgnoreCase("title")) {
                                return notification.getTitle();
                            } else if (sort.getProperty().equalsIgnoreCase("notificationStatus")) {
                                return notification.getNotificationStatus().name();
                            } else {
                                return notification.getMetadata();
                            }
                        });

                    return sort.isAscending() ? singleComparator : singleComparator.reversed();
                })
                .reduce(Comparator::thenComparing)
                .orElse(Comparator.comparing(Notification::getNotificationId)); // 기본 정렬
            // comparator 로 정렬
            notificationList = notificationList.stream()
                .sorted(comparator)
                .toList();
        }

        // 페이징 처리
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), notificationList.size());
        List<Notification> pageContent =
            (start > notificationList.size()) ? Collections.emptyList() :
                notificationList.subList(start, end);

        return new PageImpl<>(pageContent, pageable, notificationList.size());
    }

    @Override
    public Optional<Notification> findByNotificationIdAndUserId(UUID notificationId, UUID userId) {
        return notificationMap.values().stream()
            .filter(notification -> notification.getUserId().equals(userId))
            .filter(notification -> notification.getNotificationId().equals(notificationId))
            .findAny();
    }

    @Override
    public Optional<Notification> findByNotificationId(UUID notificationId) {
        return notificationMap.values().stream()
            .filter(notification -> notification.getNotificationId().equals(notificationId))
            .findAny();
    }
}
