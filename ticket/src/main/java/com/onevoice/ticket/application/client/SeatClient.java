package com.onevoice.ticket.application.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "seat-service")
public interface SeatClient {
}
