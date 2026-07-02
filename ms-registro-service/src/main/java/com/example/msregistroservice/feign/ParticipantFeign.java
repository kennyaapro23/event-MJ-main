package com.example.msregistroservice.feign;

import com.example.msregistroservice.dtos.ParticipantDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="ms-participants-service", path = "/participants")
public interface ParticipantFeign {

    @GetMapping("/{id}")
    @CircuitBreaker(name = "participanteListarPorIdCB")
    ParticipantDTO buscarPorId(@PathVariable Long id);


}

