package com.salon.customer.controller;

import com.salon.customer.dto.CustomerRequest;
import com.salon.customer.dto.CustomerResponse;
import com.salon.customer.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@Tag(name = "Customer", description = "APIs for managing salon customers")
public class CustomerController {

    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new customer")
    public CustomerResponse createCustomer(@Valid @RequestBody CustomerRequest request) {
        return service.createCustomer(request);
    }

    @GetMapping
    @Operation(summary = "Get all customers for the current salon")
    public List<CustomerResponse> getAllCustomers() {
        return service.getAllCustomers();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a specific customer by ID")
    public CustomerResponse getCustomerById(@PathVariable Long id) {
        return service.getCustomerById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing customer")
    public CustomerResponse updateCustomer(@PathVariable Long id, @Valid @RequestBody CustomerRequest request) {
        return service.updateCustomer(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Soft-delete a customer",
            description = "Marks the customer as inactive. They will no longer be retrievable via GET APIs."
    )
    public CustomerResponse deleteCustomer(@PathVariable Long id) {
        return service.deleteCustomer(id);
    }
}
