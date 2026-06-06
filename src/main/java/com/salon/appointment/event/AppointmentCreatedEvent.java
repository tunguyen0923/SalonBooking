package com.salon.appointment.event;

import java.time.LocalDateTime;
import java.util.List;

public record AppointmentCreatedEvent(
    Long appointmentId,
    Long salonId,
    String salonName,
    Long customerId,
    String customerName,
    String customerEmail,
    Long technicianId,
    String technicianName,
    String technicianEmail,
    LocalDateTime appointmentStartTime,
    LocalDateTime appointmentEndTime,
    List<String> services
) {}
