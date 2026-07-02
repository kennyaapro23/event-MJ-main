package com.emm.mseventoservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class MsEventoServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsEventoServiceApplication.class, args);
    }

}
