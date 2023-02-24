package com.duoer.reggie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication
@EnableTransactionManagement
@EnableCaching
public class ReggieTakeOutApplication {

    public static void main(String[] args) throws UnknownHostException {
        SpringApplication.run(ReggieTakeOutApplication.class, args);
        System.out.println(InetAddress.getLocalHost().getHostAddress());
    }

}
