package com.example.mspagoservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsPagoServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(MsPagoServiceApplication.class, args);
    }
}
