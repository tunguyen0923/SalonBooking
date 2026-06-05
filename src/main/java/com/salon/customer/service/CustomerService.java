package com.salon.customer.service;

import com.salon.common.exception.BadRequestException;
import com.salon.common.exception.NotFoundException;
import com.salon.common.security.SecurityUtil;
import com.salon.customer.dto.CustomerRequest;
import com.salon.customer.dto.CustomerResponse;
import com.salon.customer.entity.Customer;
import com.salon.customer.mapper.CustomerMapper;
import com.salon.customer.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private final CustomerRepository repository;
    private final CustomerMapper mapper;

    public CustomerService(CustomerRepository repository, CustomerMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional
    public CustomerResponse createCustomer(CustomerRequest request) {
        validateContact(request);
        Long salonId = SecurityUtil.getCurrentSalonId();
        Customer customer = mapper.toEntity(request, salonId);
        return mapper.toResponse(repository.save(customer));
    }

    @Transactional(readOnly = true)
    public List<CustomerResponse> getAllCustomers() {
        Long salonId = SecurityUtil.getCurrentSalonId();
        return repository.findAllBySalonIdAndActiveTrue(salonId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CustomerResponse getCustomerById(Long id) {
        return mapper.toResponse(getByIdForCurrentSalon(id));
    }

    @Transactional
    public CustomerResponse updateCustomer(Long id, CustomerRequest request) {
        validateContact(request);
        Customer customer = getByIdForCurrentSalon(id);
        mapper.updateEntity(customer, request);
        return mapper.toResponse(repository.save(customer));
    }

    @Transactional
    public CustomerResponse deleteCustomer(Long id) {
        Customer customer = getByIdForCurrentSalon(id);
        customer.setActive(false);
        return mapper.toResponse(repository.save(customer));
    }

    private Customer getByIdForCurrentSalon(Long id) {
        Long salonId = SecurityUtil.getCurrentSalonId();
        return repository.findByIdAndSalonIdAndActiveTrue(id, salonId)
                .orElseThrow(() -> new NotFoundException("Customer not found with id: " + id));
    }

    private void validateContact(CustomerRequest request) {
        if (!StringUtils.hasText(request.getEmail()) && !StringUtils.hasText(request.getPhone())) {
            throw new BadRequestException("At least one contact method (email or phone) is required");
        }
    }
}
