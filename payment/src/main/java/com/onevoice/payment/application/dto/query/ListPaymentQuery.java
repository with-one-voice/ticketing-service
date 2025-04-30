package com.onevoice.payment.application.dto.query;

import java.util.List;

public record ListPaymentQuery(
    List<FindPaymentQuery> queryList
) {

    public static ListPaymentQuery from(List<FindPaymentQuery> queryList) {
        return new ListPaymentQuery(queryList);
    }
}
