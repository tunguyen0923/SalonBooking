package com.salon.technician.controller;

import com.salon.technician.dto.TechnicianRequest;
import com.salon.technician.dto.TechnicianResponse;
import com.salon.technician.service.TechnicianService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/technicians")
@Tag(name = "Technician", description = "APIs for managing salon technicians")
public class TechnicianController {

    private final TechnicianService service;

    public TechnicianController(TechnicianService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new technician")
    public TechnicianResponse createTechnician(@Valid @RequestBody TechnicianRequest request) {
        return service.createTechnician(request);
    }

    @GetMapping
    @Operation(summary = "Get all technicians for the current salon")
    public List<TechnicianResponse> getAllTechnicians() {
        return service.getAllTechnicians();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a specific technician by ID")
    public TechnicianResponse getTechnicianById(@PathVariable Long id) {
        return service.getTechnicianById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing technician")
    public TechnicianResponse updateTechnician(@PathVariable Long id, @Valid @RequestBody TechnicianRequest request) {
        return service.updateTechnician(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Soft-delete a technician",
            description = "Marks the technician as inactive. They will no longer be retrievable via GET APIs or available for new appointments."
    )
    public TechnicianResponse deleteTechnician(@PathVariable Long id) {
        return service.deleteTechnician(id);
    }
}
