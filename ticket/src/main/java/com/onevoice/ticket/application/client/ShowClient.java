package com.onevoice.ticket.application.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "show-service")
public interface ShowClient {
}
