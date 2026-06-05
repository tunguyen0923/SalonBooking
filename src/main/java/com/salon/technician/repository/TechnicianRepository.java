package com.salon.technician.repository;

import com.salon.technician.entity.Technician;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TechnicianRepository extends JpaRepository<Technician, Long> {
    List<Technician> findAllBySalonIdAndActiveTrue(Long salonId);
    Optional<Technician> findByIdAndSalonIdAndActiveTrue(Long id, Long salonId);
}
