package com.salon.auth.service;

import com.salon.auth.dto.LoginRequest;
import com.salon.auth.dto.LoginResponse;
import com.salon.auth.dto.RegisterRequest;
import com.salon.auth.entity.User;
import com.salon.auth.repository.UserRepository;
import com.salon.common.exception.NotFoundException;
import com.salon.common.exception.UnauthorizedException;
import com.salon.common.security.JwtUtil;
import com.salon.salon.dto.SalonResponse;
import com.salon.salon.entity.Salon;
import com.salon.salon.service.SalonService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class AuthService {

    private final SalonService salonService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(SalonService salonService, UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.salonService = salonService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public SalonResponse register(RegisterRequest req) {
        Salon s = salonService.createSalon(req.salonName, req.salonEmail, req.salonPhone);

        User owner = new User();
        owner.setSalon(s);
        owner.setEmail(req.ownerEmail);
        owner.setPasswordHash(passwordEncoder.encode(req.ownerPassword));
        owner.setRole("OWNER");
        owner.setEnabled(true);
        owner.setCreatedAt(LocalDateTime.now());
        userRepository.save(owner);
        
        return new SalonResponse(s.getId(), s.getName(), s.getEmail(), s.getPhone(), s.getCreatedAt());
    }

    public LoginResponse login(LoginRequest req) {
        Salon salon = salonService.findById(req.salonId);
        if (salon == null) throw new NotFoundException("Salon not found");

        User user = userRepository.findByEmailAndSalon(req.email, salon)
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));

        if (!passwordEncoder.matches(req.password, user.getPasswordHash())) {
            throw new UnauthorizedException("Invalid credentials");
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("salonId", salon.getId());
        claims.put("role", user.getRole());

        String token = jwtUtil.generateToken(claims);

        return new LoginResponse(token, salon.getId(), user.getRole());
    }
}

