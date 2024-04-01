package com.service.marketplace;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ServiceMarketplaceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceMarketplaceApplication.class, args);
    }

}
