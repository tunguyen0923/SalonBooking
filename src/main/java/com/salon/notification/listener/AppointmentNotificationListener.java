package com.salon.notification.listener;

import com.salon.appointment.event.AppointmentCancelledEvent;
import com.salon.appointment.event.AppointmentCreatedEvent;
import com.salon.appointment.event.AppointmentRescheduledEvent;
import com.salon.notification.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AppointmentNotificationListener {

    private static final Logger log = LoggerFactory.getLogger(AppointmentNotificationListener.class);

    private final NotificationService notificationService;

    public AppointmentNotificationListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Async
    @EventListener
    public void onAppointmentCreated(AppointmentCreatedEvent event) {
        log.info("AppointmentCreatedEvent received for appointment: {}", event.appointmentId());
        
        // Notify Customer
        if (event.customerEmail() != null) {
            notificationService.sendAppointmentConfirmation(
                    event.customerEmail(),
                    event.customerName(),
                    event.technicianName(),
                    event.appointmentStartTime(),
                    event.services(),
                    event.salonName()
            );
        }

        // Notify Technician
        if (event.technicianEmail() != null) {
            notificationService.sendTechnicianNewAssignment(
                    event.technicianEmail(),
                    event.technicianName(),
                    event.customerName(),
                    event.appointmentStartTime(),
                    event.services(),
                    event.salonName()
            );
        }
    }

    @Async
    @EventListener
    public void onAppointmentCancelled(AppointmentCancelledEvent event) {
        log.info("AppointmentCancelledEvent received for appointment: {}", event.appointmentId());

        // Notify Customer
        if (event.customerEmail() != null) {
            notificationService.sendAppointmentCancellation(
                    event.customerEmail(),
                    event.customerName(),
                    event.appointmentId(),
                    false,
                    event.salonName()
            );
        }

        // Notify Technician
        if (event.technicianEmail() != null) {
            notificationService.sendAppointmentCancellation(
                    event.technicianEmail(),
                    event.technicianName(),
                    event.appointmentId(),
                    true,
                    event.salonName()
            );
        }
    }

    @Async
    @EventListener
    public void onAppointmentRescheduled(AppointmentRescheduledEvent event) {
        log.info("AppointmentRescheduledEvent received for appointment: {}", event.appointmentId());

        // Notify Customer
        if (event.customerEmail() != null) {
            notificationService.sendAppointmentRescheduled(
                    event.customerEmail(),
                    event.customerName(),
                    event.appointmentId(),
                    event.oldStartTime(),
                    event.newStartTime(),
                    event.salonName()
            );
        }

        // Notify Technician
        if (event.technicianEmail() != null) {
            notificationService.sendAppointmentRescheduled(
                    event.technicianEmail(),
                    event.technicianName(),
                    event.appointmentId(),
                    event.oldStartTime(),
                    event.newStartTime(),
                    event.salonName()
            );
        }
    }
}
