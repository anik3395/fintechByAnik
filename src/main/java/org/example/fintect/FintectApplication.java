package org.example.fintect;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching
@EnableScheduling
public class FintectApplication {

    public static void main(String[] args) {
        SpringApplication.run(FintectApplication.class, args);
    }

}
