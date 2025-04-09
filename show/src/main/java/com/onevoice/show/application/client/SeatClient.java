package com.onevoice.show.application.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "seat-service")
public interface SeatClient {

}
