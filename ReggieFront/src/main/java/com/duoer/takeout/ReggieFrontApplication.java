package com.duoer.takeout;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class ReggieFrontApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReggieFrontApplication.class, args);
    }
}
