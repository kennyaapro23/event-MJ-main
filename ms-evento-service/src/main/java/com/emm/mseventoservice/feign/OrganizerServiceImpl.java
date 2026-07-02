package com.emm.mseventoservice.feign;


import com.emm.mseventoservice.dtos.AuthUserDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrganizerServiceImpl {

    private final OrganizerFeignClient organizerFeignClient;

    public OrganizerServiceImpl(OrganizerFeignClient organizerFeignClient) {
        this.organizerFeignClient = organizerFeignClient;
    }

    @CircuitBreaker(name = "organizerClientCB", fallbackMethod = "fallbackGetUserById")
    public AuthUserDto getUserById(Long id) {
        return organizerFeignClient.getUserById(id);
    }

    @CircuitBreaker(name = "organizerClientCB", fallbackMethod = "fallbackExistsById")
    public Boolean existsById(Long id) {
        return organizerFeignClient.existsById(id);
    }

    public AuthUserDto fallbackGetUserById(Long id, Throwable ex) {
        log.warn("Fallback getUserById activado para id {}: {}", id, ex.getMessage());
        return null;
    }

    public Boolean fallbackExistsById(Long id, Throwable ex) {
        log.warn("Fallback existsById activado para id {}: {}", id, ex.getMessage());
        return false;
    }
}
