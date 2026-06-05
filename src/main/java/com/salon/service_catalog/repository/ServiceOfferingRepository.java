package com.salon.service_catalog.repository;

import com.salon.service_catalog.entity.ServiceOffering;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceOfferingRepository extends JpaRepository<ServiceOffering, Long> {
    List<ServiceOffering> findAllBySalonIdAndActiveTrue(Long salonId);
    Optional<ServiceOffering> findByIdAndSalonIdAndActiveTrue(Long id, Long salonId);
}
