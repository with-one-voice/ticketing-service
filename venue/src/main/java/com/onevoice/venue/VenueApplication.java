package com.onevoice.venue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class VenueApplication {

	public static void main(String[] args) {
		SpringApplication.run(VenueApplication.class, args);
	}

}
