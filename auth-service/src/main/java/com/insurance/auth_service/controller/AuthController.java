package com.insurance.auth_service.controller;
import com.insurance.auth_service.dto.*;
import com.insurance.auth_service.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService s) { this.authService = s; }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest r) {
        authService.register(r);
        return ResponseEntity.ok(Map.of("message", "User registered"));
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest r) {
        return ResponseEntity.ok(authService.login(r));
    }
}