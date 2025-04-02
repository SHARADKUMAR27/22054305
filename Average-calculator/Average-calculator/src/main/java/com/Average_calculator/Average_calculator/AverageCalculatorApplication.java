package com.Average_calculator.Average_calculator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@SpringBootApplication
public class AverageCalculatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(AverageCalculatorApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .setConnectTimeout(Duration.ofMillis(500))
                .setReadTimeout(Duration.ofMillis(500))
                .build();
    }
}