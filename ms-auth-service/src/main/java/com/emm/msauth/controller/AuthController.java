package com.emm.msauth.controller;

import com.emm.msauth.dto.AuthResponse;
import com.emm.msauth.dto.AuthUserDto;
import com.emm.msauth.dto.LoginRequest;
import com.emm.msauth.dto.RegisterRequest;
import com.emm.msauth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @GetMapping("/{id}")
    public AuthUserDto getById(@PathVariable Long id) {
        return authService.getById(id);
    }

    @GetMapping("/{id}/exists")
    public Boolean exists(@PathVariable Long id) {
        return authService.existsById(id);
    }
}
