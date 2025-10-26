package com.hexalyte.salon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SalonManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(SalonManagementApplication.class, args);
    }

}


