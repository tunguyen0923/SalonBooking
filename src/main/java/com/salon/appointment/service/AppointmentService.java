package com.salon.appointment.service;

import com.salon.appointment.dto.AppointmentRequest;
import com.salon.appointment.dto.AppointmentResponse;
import com.salon.appointment.entity.Appointment;
import com.salon.appointment.entity.AppointmentServiceItem;
import com.salon.appointment.entity.AppointmentStatus;
import com.salon.appointment.event.AppointmentCancelledEvent;
import com.salon.appointment.event.AppointmentCreatedEvent;
import com.salon.appointment.event.AppointmentRescheduledEvent;
import com.salon.appointment.mapper.AppointmentMapper;
import com.salon.appointment.repository.AppointmentRepository;
import com.salon.common.event.DomainEventPublisher;
import com.salon.common.exception.ConflictException;
import com.salon.common.exception.NotFoundException;
import com.salon.common.security.SecurityUtil;
import com.salon.customer.entity.Customer;
import com.salon.customer.repository.CustomerRepository;
import com.salon.salon.entity.Salon;
import com.salon.salon.repository.SalonRepository;
import com.salon.service_catalog.entity.ServiceOffering;
import com.salon.service_catalog.repository.ServiceOfferingRepository;
import com.salon.technician.entity.Technician;
import com.salon.technician.repository.TechnicianRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    private static final Logger log = LoggerFactory.getLogger(AppointmentService.class);

    private final AppointmentRepository repository;
    private final CustomerRepository customerRepository;
    private final TechnicianRepository technicianRepository;
    private final ServiceOfferingRepository serviceRepository;
    private final SalonRepository salonRepository;
    private final AppointmentMapper mapper;
    private final DomainEventPublisher eventPublisher;

    public AppointmentService(AppointmentRepository repository,
                              CustomerRepository customerRepository,
                              TechnicianRepository technicianRepository,
                              ServiceOfferingRepository serviceRepository,
                              SalonRepository salonRepository,
                              AppointmentMapper mapper,
                              DomainEventPublisher eventPublisher) {
        this.repository = repository;
        this.customerRepository = customerRepository;
        this.technicianRepository = technicianRepository;
        this.serviceRepository = serviceRepository;
        this.salonRepository = salonRepository;
        this.mapper = mapper;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public AppointmentResponse createAppointment(AppointmentRequest request) {
        Long salonId = SecurityUtil.getCurrentSalonId();

        // 1. Validate Customer
        Customer customer = customerRepository.findByIdAndSalonIdAndActiveTrue(request.getCustomerId(), salonId)
                .orElseThrow(() -> new NotFoundException("Customer not found or does not belong to your salon"));

        // 2. Validate Technician
        Technician technician = technicianRepository.findByIdAndSalonIdAndActiveTrue(request.getTechnicianId(), salonId)
                .orElseThrow(() -> new NotFoundException("Technician not found or does not belong to your salon"));

        // 3. Validate Services and Calculate Duration
        List<ServiceOffering> services = request.getServiceIds().stream()
                .map(id -> serviceRepository.findByIdAndSalonIdAndActiveTrue(id, salonId)
                        .orElseThrow(() -> new NotFoundException("Service not found or is inactive: " + id)))
                .collect(Collectors.toList());

        int totalDuration = services.stream().mapToInt(ServiceOffering::getDurationMinutes).sum();
        LocalDateTime endTime = request.getStartTime().plusMinutes(totalDuration);

        // 4. Check for double booking
        checkOverlap(request.getTechnicianId(), request.getStartTime(), endTime, null);

        // 5. Create Appointment
        Appointment appointment = new Appointment();
        appointment.setSalonId(salonId);
        appointment.setCustomerId(request.getCustomerId());
        appointment.setTechnicianId(request.getTechnicianId());
        appointment.setStartTime(request.getStartTime());
        appointment.setEndTime(endTime);
        appointment.setStatus(AppointmentStatus.BOOKED);
        appointment.setNotes(request.getNotes());

        List<AppointmentServiceItem> serviceItems = services.stream()
                .map(s -> new AppointmentServiceItem(appointment, s.getId(), s.getPrice(), s.getDurationMinutes()))
                .collect(Collectors.toList());
        
        appointment.getServices().addAll(serviceItems);
        
        Appointment saved = repository.save(appointment);
        
        // Fetch Salon details
        Salon salon = salonRepository.findById(salonId).orElse(null);
        String salonName = salon != null ? salon.getName() : "Our Salon";
        
        // Publish Event
        log.info("Publishing AppointmentCreatedEvent for appointment: {}", saved.getId());
        eventPublisher.publish(new AppointmentCreatedEvent(
                saved.getId(),
                saved.getSalonId(),
                salonName,
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                technician.getId(),
                technician.getName(),
                technician.getEmail(),
                saved.getStartTime(),
                saved.getEndTime(),
                services.stream().map(ServiceOffering::getName).collect(Collectors.toList())
        ));
        
        return mapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<AppointmentResponse> getAllAppointments() {
        Long salonId = SecurityUtil.getCurrentSalonId();
        return repository.findAllBySalonId(salonId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AppointmentResponse getAppointmentById(Long id) {
        return mapper.toResponse(getByIdForCurrentSalon(id));
    }

    @Transactional
    public AppointmentResponse cancelAppointment(Long id) {
        Appointment appointment = getByIdForCurrentSalon(id);
        appointment.setStatus(AppointmentStatus.CANCELLED);
        Appointment saved = repository.save(appointment);

        // Fetch details for event
        Customer customer = customerRepository.findById(saved.getCustomerId()).orElse(null);
        Technician technician = technicianRepository.findById(saved.getTechnicianId()).orElse(null);
        Salon salon = salonRepository.findById(saved.getSalonId()).orElse(null);
        String salonName = salon != null ? salon.getName() : "Our Salon";

        log.info("Publishing AppointmentCancelledEvent for appointment: {}", saved.getId());
        eventPublisher.publish(new AppointmentCancelledEvent(
                saved.getId(),
                saved.getSalonId(),
                salonName,
                saved.getCustomerId(),
                customer != null ? customer.getName() : "Unknown",
                customer != null ? customer.getEmail() : null,
                saved.getTechnicianId(),
                technician != null ? technician.getName() : "Unknown",
                technician != null ? technician.getEmail() : null
        ));

        return mapper.toResponse(saved);
    }

    @Transactional
    public AppointmentResponse rescheduleAppointment(Long id, LocalDateTime newStartTime) {
        Appointment appointment = getByIdForCurrentSalon(id);
        LocalDateTime oldStartTime = appointment.getStartTime();
        
        int totalDuration = appointment.getServices().stream()
                .mapToInt(AppointmentServiceItem::getDurationSnapshot)
                .sum();
        LocalDateTime newEndTime = newStartTime.plusMinutes(totalDuration);

        checkOverlap(appointment.getTechnicianId(), newStartTime, newEndTime, appointment.getId());

        appointment.setStartTime(newStartTime);
        appointment.setEndTime(newEndTime);
        appointment.setStatus(AppointmentStatus.BOOKED); // Reset status to booked on reschedule?
        
        Appointment saved = repository.save(appointment);

        // Fetch details for event
        Customer customer = customerRepository.findById(saved.getCustomerId()).orElse(null);
        Technician technician = technicianRepository.findById(saved.getTechnicianId()).orElse(null);
        Salon salon = salonRepository.findById(saved.getSalonId()).orElse(null);
        String salonName = salon != null ? salon.getName() : "Our Salon";

        log.info("Publishing AppointmentRescheduledEvent for appointment: {}", saved.getId());
        eventPublisher.publish(new AppointmentRescheduledEvent(
                saved.getId(),
                saved.getSalonId(),
                salonName,
                saved.getCustomerId(),
                customer != null ? customer.getName() : "Unknown",
                customer != null ? customer.getEmail() : null,
                saved.getTechnicianId(),
                technician != null ? technician.getName() : "Unknown",
                technician != null ? technician.getEmail() : null,
                oldStartTime,
                saved.getStartTime(),
                saved.getEndTime()
        ));
        
        return mapper.toResponse(saved);
    }

    private Appointment getByIdForCurrentSalon(Long id) {
        Long salonId = SecurityUtil.getCurrentSalonId();
        return repository.findByIdAndSalonId(id, salonId)
                .orElseThrow(() -> new NotFoundException("Appointment not found with id: " + id));
    }

    private void checkOverlap(Long techId, LocalDateTime start, LocalDateTime end, Long excludeAppointmentId) {
        List<Appointment> overlaps = repository.findOverlappingAppointments(techId, start, end, AppointmentStatus.CANCELLED);
        
        if (excludeAppointmentId != null) {
            overlaps = overlaps.stream()
                    .filter(a -> !a.getId().equals(excludeAppointmentId))
                    .collect(Collectors.toList());
        }

        if (!overlaps.isEmpty()) {
            throw new ConflictException("Technician is already booked for this time slot");
        }
    }
}
