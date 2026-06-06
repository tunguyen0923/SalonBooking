package com.salon.notification.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    @Value("${notification.email.enabled:true}")
    private boolean emailEnabled;

    private final EmailService emailService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy 'at' hh:mm a");

    public NotificationService(EmailService emailService) {
        this.emailService = emailService;
    }

    public void sendAppointmentConfirmation(String customerEmail, String customerName, String technicianName, 
                                           LocalDateTime startTime, List<String> services, String salonName) {
        if (!emailEnabled) {
            log.info("Email notifications are disabled. Skipping confirmation for: {}", customerEmail);
            return;
        }

        String subject = String.format("Appointment Confirmation - %s", salonName);
        String body = String.format("Hello %s,\n\nYour appointment at %s with %s is confirmed for %s.\nServices: %s\n\nThank you for choosing us!",
                customerName, salonName, technicianName, startTime.format(formatter), String.join(", ", services));

        emailService.sendEmail(customerEmail, subject, body);
    }

    public void sendTechnicianNewAssignment(String technicianEmail, String technicianName, String customerName, 
                                          LocalDateTime startTime, List<String> services, String salonName) {
        if (!emailEnabled || technicianEmail == null) return;

        String subject = String.format("New Appointment Assigned - %s", salonName);
        String body = String.format("Hello %s,\n\nYou have a new appointment at %s with %s at %s.\nServices: %s",
                technicianName, salonName, customerName, startTime.format(formatter), String.join(", ", services));

        emailService.sendEmail(technicianEmail, subject, body);
    }

    public void sendAppointmentCancellation(String email, String name, Long appointmentId, boolean isTechnician, String salonName) {
        if (!emailEnabled || email == null) return;

        String subject = String.format("Appointment Cancelled - %s", salonName);
        String body = String.format("Hello %s,\n\nAppointment #%d at %s has been cancelled.", name, appointmentId, salonName);

        emailService.sendEmail(email, subject, body);
    }

    public void sendAppointmentRescheduled(String email, String name, Long appointmentId, 
                                         LocalDateTime oldTime, LocalDateTime newTime, String salonName) {
        if (!emailEnabled || email == null) return;

        String subject = String.format("Appointment Rescheduled - %s", salonName);
        String body = String.format("Hello %s,\n\nYour appointment #%d at %s has been rescheduled from %s to %s.", 
                name, appointmentId, salonName, oldTime.format(formatter), newTime.format(formatter));

        emailService.sendEmail(email, subject, body);
    }
}
