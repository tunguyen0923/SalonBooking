package com.salon.notification.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private EmailService emailService;

    @InjectMocks
    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(notificationService, "emailEnabled", true);
    }

    @Test
    void shouldSendConfirmationWhenEnabled() {
        String email = "customer@test.com";
        String customer = "Customer";
        String tech = "Tech";
        LocalDateTime time = LocalDateTime.now();
        List<String> services = Collections.singletonList("Cut");

        notificationService.sendAppointmentConfirmation(email, customer, tech, time, services);

        verify(emailService).sendEmail(eq(email), anyString(), anyString());
    }

    @Test
    void shouldNotSendWhenDisabled() {
        ReflectionTestUtils.setField(notificationService, "emailEnabled", false);
        
        notificationService.sendAppointmentConfirmation("test@test.com", "C", "T", LocalDateTime.now(), Collections.emptyList());

        verify(emailService, never()).sendEmail(anyString(), anyString(), anyString());
    }
}
