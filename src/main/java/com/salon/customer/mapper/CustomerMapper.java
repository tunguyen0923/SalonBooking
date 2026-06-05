package com.salon.customer.mapper;

import com.salon.customer.dto.CustomerRequest;
import com.salon.customer.dto.CustomerResponse;
import com.salon.customer.entity.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public Customer toEntity(CustomerRequest request, Long salonId) {
        Customer customer = new Customer();
        customer.setSalonId(salonId);
        updateEntity(customer, request);
        return customer;
    }

    public void updateEntity(Customer customer, CustomerRequest request) {
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setNotes(request.getNotes());
    }

    public CustomerResponse toResponse(Customer customer) {
        CustomerResponse response = new CustomerResponse();
        response.setId(customer.getId());
        response.setSalonId(customer.getSalonId());
        response.setName(customer.getName());
        response.setEmail(customer.getEmail());
        response.setPhone(customer.getPhone());
        response.setNotes(customer.getNotes());
        response.setActive(customer.isActive());
        response.setCreatedAt(customer.getCreatedAt());
        response.setUpdatedAt(customer.getUpdatedAt());
        return response;
    }
}
