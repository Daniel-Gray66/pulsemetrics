package com.pulsemetrics.pulsemetrics.controller;

import com.pulsemetrics.pulsemetrics.dto.request.RegisterRequest;
import com.pulsemetrics.pulsemetrics.dto.response.AuthResponse;
import com.pulsemetrics.pulsemetrics.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}