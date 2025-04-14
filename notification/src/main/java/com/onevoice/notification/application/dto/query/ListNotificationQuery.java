package com.onevoice.notification.application.dto.query;

import java.util.List;

public record ListNotificationQuery(
    List<FindNotificationQuery> queryList
) {

}
