package com.salon.auth.dto;

public class LoginResponse {
    public String accessToken;
    public Long salonId;
    public String role;

    public LoginResponse(String accessToken, Long salonId, String role) {
        this.accessToken = accessToken;
        this.salonId = salonId;
        this.role = role;
    }
}

