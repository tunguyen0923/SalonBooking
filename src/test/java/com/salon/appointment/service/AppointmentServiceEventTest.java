package com.salon.appointment.service;

import com.salon.appointment.dto.AppointmentRequest;
import com.salon.appointment.dto.AppointmentResponse;
import com.salon.appointment.entity.Appointment;
import com.salon.appointment.entity.AppointmentStatus;
import com.salon.appointment.event.AppointmentCancelledEvent;
import com.salon.appointment.event.AppointmentCreatedEvent;
import com.salon.appointment.event.AppointmentRescheduledEvent;
import com.salon.appointment.mapper.AppointmentMapper;
import com.salon.appointment.repository.AppointmentRepository;
import com.salon.common.event.DomainEventPublisher;
import com.salon.common.security.UserPrincipal;
import com.salon.customer.entity.Customer;
import com.salon.customer.repository.CustomerRepository;
import com.salon.service_catalog.entity.ServiceOffering;
import com.salon.service_catalog.repository.ServiceOfferingRepository;
import com.salon.technician.entity.Technician;
import com.salon.technician.repository.TechnicianRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceEventTest {

    @Mock
    private AppointmentRepository repository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private TechnicianRepository technicianRepository;
    @Mock
    private ServiceOfferingRepository serviceRepository;
    @Mock
    private AppointmentMapper mapper;
    @Mock
    private DomainEventPublisher eventPublisher;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private Authentication authentication;

    @InjectMocks
    private AppointmentService appointmentService;

    private final Long salonId = 1L;
    private final UserPrincipal principal = new UserPrincipal(1L, salonId, "OWNER");

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(principal);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldPublishAppointmentCreatedEvent() {
        // Arrange
        AppointmentRequest request = new AppointmentRequest();
        request.setCustomerId(1L);
        request.setTechnicianId(1L);
        request.setStartTime(LocalDateTime.now());
        request.setServiceIds(Collections.singletonList(1L));

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("Customer");
        customer.setEmail("customer@test.com");

        Technician technician = new Technician();
        technician.setId(1L);
        technician.setName("Tech");
        technician.setEmail("tech@test.com");

        ServiceOffering service = new ServiceOffering();
        service.setId(1L);
        service.setName("Service");
        service.setPrice(java.math.BigDecimal.TEN);
        service.setDurationMinutes(30);

        Appointment saved = new Appointment();
        saved.setId(100L);
        saved.setSalonId(salonId);
        saved.setCustomerId(1L);
        saved.setTechnicianId(1L);
        saved.setStartTime(request.getStartTime());
        saved.setEndTime(request.getStartTime().plusMinutes(30));

        when(customerRepository.findByIdAndSalonIdAndActiveTrue(1L, salonId)).thenReturn(Optional.of(customer));
        when(technicianRepository.findByIdAndSalonIdAndActiveTrue(1L, salonId)).thenReturn(Optional.of(technician));
        when(serviceRepository.findByIdAndSalonIdAndActiveTrue(1L, salonId)).thenReturn(Optional.of(service));
        when(repository.save(any())).thenReturn(saved);

        // Act
        appointmentService.createAppointment(request);

        // Assert
        ArgumentCaptor<AppointmentCreatedEvent> captor = ArgumentCaptor.forClass(AppointmentCreatedEvent.class);
        verify(eventPublisher).publish(captor.capture());
        
        AppointmentCreatedEvent event = captor.getValue();
        assertEquals(100L, event.appointmentId());
        assertEquals("Customer", event.customerName());
        assertEquals("Tech", event.technicianName());
        assertEquals(Collections.singletonList("Service"), event.services());
    }

    @Test
    void shouldPublishAppointmentCancelledEvent() {
        // Arrange
        Appointment appointment = new Appointment();
        appointment.setId(100L);
        appointment.setSalonId(salonId);
        appointment.setCustomerId(1L);
        appointment.setTechnicianId(1L);
        appointment.setStatus(AppointmentStatus.BOOKED);

        Customer customer = new Customer();
        customer.setName("Customer");
        customer.setEmail("customer@test.com");

        Technician technician = new Technician();
        technician.setName("Tech");
        technician.setEmail("tech@test.com");

        when(repository.findByIdAndSalonId(100L, salonId)).thenReturn(Optional.of(appointment));
        when(repository.save(any())).thenReturn(appointment);
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(technicianRepository.findById(1L)).thenReturn(Optional.of(technician));

        // Act
        appointmentService.cancelAppointment(100L);

        // Assert
        ArgumentCaptor<AppointmentCancelledEvent> captor = ArgumentCaptor.forClass(AppointmentCancelledEvent.class);
        verify(eventPublisher).publish(captor.capture());
        
        AppointmentCancelledEvent event = captor.getValue();
        assertEquals(100L, event.appointmentId());
        assertEquals("Customer", event.customerName());
        assertEquals("Tech", event.technicianName());
    }
}
