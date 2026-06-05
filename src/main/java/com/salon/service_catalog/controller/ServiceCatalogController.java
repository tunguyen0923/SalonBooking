package com.salon.service_catalog.controller;

import com.salon.service_catalog.dto.ServiceRequest;
import com.salon.service_catalog.dto.ServiceResponse;
import com.salon.service_catalog.service.ServiceCatalogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
@Tag(name = "Service Catalog", description = "APIs for managing salon services")
public class ServiceCatalogController {

    private final ServiceCatalogService service;

    public ServiceCatalogController(ServiceCatalogService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new service offering")
    public ServiceResponse createService(@Valid @RequestBody ServiceRequest request) {
        return service.createService(request);
    }

    @GetMapping
    @Operation(summary = "Get all service offerings for the current salon")
    public List<ServiceResponse> getAllServices() {
        return service.getAllServices();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a specific service offering by ID")
    public ServiceResponse getServiceById(@PathVariable Long id) {
        return service.getServiceById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing service offering")
    public ServiceResponse updateService(@PathVariable Long id, @Valid @RequestBody ServiceRequest request) {
        return service.updateService(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Soft-delete a service offering",
            description = "Marks the service as inactive. It will no longer be retrievable via GET APIs or available for new bookings."
    )
    public ServiceResponse deleteService(@PathVariable Long id) {
        return service.deleteService(id);
    }
}
