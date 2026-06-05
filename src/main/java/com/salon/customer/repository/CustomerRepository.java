package com.salon.customer.repository;

import com.salon.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findAllBySalonIdAndActiveTrue(Long salonId);
    Optional<Customer> findByIdAndSalonIdAndActiveTrue(Long id, Long salonId);
}
