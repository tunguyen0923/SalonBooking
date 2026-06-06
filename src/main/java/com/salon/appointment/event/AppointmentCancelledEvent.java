package com.salon.appointment.event;

public record AppointmentCancelledEvent(
    Long appointmentId,
    Long salonId,
    String salonName,
    Long customerId,
    String customerName,
    String customerEmail,
    Long technicianId,
    String technicianName,
    String technicianEmail
) {}
