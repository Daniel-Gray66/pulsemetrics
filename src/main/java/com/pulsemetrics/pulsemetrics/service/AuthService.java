package com.pulsemetrics.pulsemetrics.service;

import com.pulsemetrics.pulsemetrics.dto.request.RegisterRequest;
import com.pulsemetrics.pulsemetrics.dto.response.AuthResponse;
import com.pulsemetrics.pulsemetrics.model.Role;
import com.pulsemetrics.pulsemetrics.model.entity.User;
import com.pulsemetrics.pulsemetrics.repository.UserRepository;
import com.pulsemetrics.pulsemetrics.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already in use");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(user);
        String token = jwtService.generateToken(user.getUsername());
        return AuthResponse.builder().token(token).username(user.getUsername()).build();
    }

    public AuthResponse login(RegisterRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtService.generateToken(user.getUsername());
        return AuthResponse.builder().token(token).username(user.getUsername()).build();
    }
}