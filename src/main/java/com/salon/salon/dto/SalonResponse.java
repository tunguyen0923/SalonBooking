package com.salon.salon.dto;

import java.time.LocalDateTime;

public class SalonResponse {
    public Long id;
    public String name;
    public String email;
    public String phone;
    public LocalDateTime createdAt;

    public SalonResponse(Long id, String name, String email, String phone, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.createdAt = createdAt;
    }
}
