package com.emm.mseventoservice.feign;

import com.emm.mseventoservice.dtos.AuthUserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-auth-service", path = "/auth")
public interface OrganizerFeignClient {

    @GetMapping("/{id}")
    AuthUserDto getUserById(@PathVariable("id") Long id);

    @GetMapping("/{id}/exists")
    Boolean existsById(@PathVariable("id") Long id);
}
