package com.salon.appointment.service;

import com.salon.appointment.dto.AppointmentRequest;
import com.salon.appointment.dto.AppointmentResponse;
import com.salon.appointment.entity.Appointment;
import com.salon.appointment.entity.AppointmentServiceItem;
import com.salon.appointment.entity.AppointmentStatus;
import com.salon.appointment.mapper.AppointmentMapper;
import com.salon.appointment.repository.AppointmentRepository;
import com.salon.common.exception.ConflictException;
import com.salon.common.exception.NotFoundException;
import com.salon.common.security.SecurityUtil;
import com.salon.customer.repository.CustomerRepository;
import com.salon.service_catalog.entity.ServiceOffering;
import com.salon.service_catalog.repository.ServiceOfferingRepository;
import com.salon.technician.repository.TechnicianRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    private final AppointmentRepository repository;
    private final CustomerRepository customerRepository;
    private final TechnicianRepository technicianRepository;
    private final ServiceOfferingRepository serviceRepository;
    private final AppointmentMapper mapper;

    public AppointmentService(AppointmentRepository repository,
                              CustomerRepository customerRepository,
                              TechnicianRepository technicianRepository,
                              ServiceOfferingRepository serviceRepository,
                              AppointmentMapper mapper) {
        this.repository = repository;
        this.customerRepository = customerRepository;
        this.technicianRepository = technicianRepository;
        this.serviceRepository = serviceRepository;
        this.mapper = mapper;
    }

    @Transactional
    public AppointmentResponse createAppointment(AppointmentRequest request) {
        Long salonId = SecurityUtil.getCurrentSalonId();

        // 1. Validate Customer
        customerRepository.findByIdAndSalonIdAndActiveTrue(request.getCustomerId(), salonId)
                .orElseThrow(() -> new NotFoundException("Customer not found or does not belong to your salon"));

        // 2. Validate Technician
        technicianRepository.findByIdAndSalonIdAndActiveTrue(request.getTechnicianId(), salonId)
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
        
        return mapper.toResponse(repository.save(appointment));
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
        return mapper.toResponse(repository.save(appointment));
    }

    @Transactional
    public AppointmentResponse rescheduleAppointment(Long id, LocalDateTime newStartTime) {
        Appointment appointment = getByIdForCurrentSalon(id);
        
        int totalDuration = appointment.getServices().stream()
                .mapToInt(AppointmentServiceItem::getDurationSnapshot)
                .sum();
        LocalDateTime newEndTime = newStartTime.plusMinutes(totalDuration);

        checkOverlap(appointment.getTechnicianId(), newStartTime, newEndTime, appointment.getId());

        appointment.setStartTime(newStartTime);
        appointment.setEndTime(newEndTime);
        appointment.setStatus(AppointmentStatus.BOOKED); // Reset status to booked on reschedule?
        
        return mapper.toResponse(repository.save(appointment));
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
