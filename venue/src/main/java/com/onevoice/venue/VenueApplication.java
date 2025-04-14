package com.onevoice.venue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication(scanBasePackages = {"com.onevoice.common", "com.onevoice.venue"})
public class VenueApplication {

    public static void main(String[] args) {
        SpringApplication.run(VenueApplication.class, args);
    }

}
