package com.onevoice.payment.application.dto.query;

import java.util.List;

public record ListPaymentQuery(
    List<FindPaymentQuery> queryList
) {

}
