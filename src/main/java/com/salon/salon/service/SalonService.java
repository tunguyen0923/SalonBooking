package com.salon.salon.service;

import com.salon.salon.entity.Salon;
import com.salon.salon.repository.SalonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class SalonService {

    private final SalonRepository salonRepository;

    public SalonService(SalonRepository salonRepository) {
        this.salonRepository = salonRepository;
    }

    public Salon createSalon(String name, String email, String phone) {
        Salon s = new Salon();
        s.setName(name);
        s.setEmail(email);
        s.setPhone(phone);
        s.setCreatedAt(LocalDateTime.now());
        return salonRepository.save(s);
    }

    public Salon findById(Long id) {
        return salonRepository.findById(id).orElse(null);
    }
}

