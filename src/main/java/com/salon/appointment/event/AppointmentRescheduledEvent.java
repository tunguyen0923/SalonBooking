package com.salon.appointment.event;

import java.time.LocalDateTime;

public record AppointmentRescheduledEvent(
    Long appointmentId,
    Long salonId,
    String salonName,
    Long customerId,
    String customerName,
    String customerEmail,
    Long technicianId,
    String technicianName,
    String technicianEmail,
    LocalDateTime oldStartTime,
    LocalDateTime newStartTime,
    LocalDateTime newEndTime
) {}
