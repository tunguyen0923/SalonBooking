package com.salon.auth.controller;

import com.salon.auth.dto.LoginRequest;
import com.salon.auth.dto.LoginResponse;
import com.salon.auth.dto.RegisterRequest;
import com.salon.auth.service.AuthService;
import com.salon.salon.dto.SalonResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<SalonResponse> register(@RequestBody RegisterRequest req) {
        SalonResponse salonResponse = authService.register(req);
        return ResponseEntity.ok(salonResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest req) {
        return ResponseEntity.ok(authService.login(req));
    }
}

