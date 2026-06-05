package com.salon.appointment.controller;

import com.salon.appointment.dto.AppointmentRequest;
import com.salon.appointment.dto.AppointmentResponse;
import com.salon.appointment.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@Tag(name = "Appointment", description = "APIs for managing salon appointments")
public class AppointmentController {

    private final AppointmentService service;

    public AppointmentController(AppointmentService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new appointment")
    public AppointmentResponse createAppointment(@Valid @RequestBody AppointmentRequest request) {
        return service.createAppointment(request);
    }

    @GetMapping
    @Operation(summary = "Get all appointments for the current salon")
    public List<AppointmentResponse> getAllAppointments() {
        return service.getAllAppointments();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a specific appointment by ID")
    public AppointmentResponse getAppointmentById(@PathVariable Long id) {
        return service.getAppointmentById(id);
    }

    @PutMapping("/{id}/cancel")
    @Operation(summary = "Cancel an appointment")
    public AppointmentResponse cancelAppointment(@PathVariable Long id) {
        return service.cancelAppointment(id);
    }

    @PutMapping("/{id}/reschedule")
    @Operation(summary = "Reschedule an existing appointment")
    public AppointmentResponse rescheduleAppointment(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime newStartTime) {
        return service.rescheduleAppointment(id, newStartTime);
    }
}
