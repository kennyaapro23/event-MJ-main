package com.emm.msparticipants;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication
public class MsParticipantsApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsParticipantsApplication.class, args);
    }

}
