package com.currencyconverter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

import org.springframework.scheduling.annotation.EnableScheduling;

@EnableSwagger2
@SpringBootApplication
@EnableScheduling // Enable scheduling for @Scheduled annotations
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    // Define RestTemplate Bean
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
