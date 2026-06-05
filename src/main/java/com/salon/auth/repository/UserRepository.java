package com.salon.auth.repository;

import com.salon.auth.entity.User;
import com.salon.salon.entity.Salon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAndSalon(String email, Salon salon);
}

