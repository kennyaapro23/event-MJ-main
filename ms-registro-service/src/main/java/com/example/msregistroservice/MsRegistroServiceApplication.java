package com.example.msregistroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients

@SpringBootApplication
public class MsRegistroServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsRegistroServiceApplication.class, args);
    }

}

