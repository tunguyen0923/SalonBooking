package com.salon.appointment.mapper;

import com.salon.appointment.dto.AppointmentResponse;
import com.salon.appointment.entity.Appointment;
import com.salon.appointment.entity.AppointmentServiceItem;
import com.salon.service_catalog.entity.ServiceOffering;
import com.salon.service_catalog.repository.ServiceOfferingRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@Component
public class AppointmentMapper {

    private final ServiceOfferingRepository serviceRepository;

    public AppointmentMapper(ServiceOfferingRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    public AppointmentResponse toResponse(Appointment appointment) {
        AppointmentResponse response = new AppointmentResponse();
        response.setId(appointment.getId());
        response.setSalonId(appointment.getSalonId());
        response.setCustomerId(appointment.getCustomerId());
        response.setTechnicianId(appointment.getTechnicianId());
        response.setStartTime(appointment.getStartTime());
        response.setEndTime(appointment.getEndTime());
        response.setStatus(appointment.getStatus());
        response.setNotes(appointment.getNotes());

        response.setServices(appointment.getServices().stream()
                .map(this::toServiceResponse)
                .collect(Collectors.toList()));

        response.setTotalAmount(appointment.getServices().stream()
                .map(AppointmentServiceItem::getPriceSnapshot)
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        return response;
    }

    private AppointmentResponse.AppointmentServiceResponse toServiceResponse(AppointmentServiceItem item) {
        AppointmentResponse.AppointmentServiceResponse serviceResponse = new AppointmentResponse.AppointmentServiceResponse();
        serviceResponse.setServiceId(item.getId().getServiceId());
        serviceResponse.setPrice(item.getPriceSnapshot());
        serviceResponse.setDuration(item.getDurationSnapshot());

        // Try to get current name, though snapshot should ideally have it.
        // Rule: Join table only has price and duration snapshots.
        // We can fetch name from ServiceOffering for display, but it might have changed or been deleted.
        serviceRepository.findById(item.getId().getServiceId())
                .ifPresent(s -> serviceResponse.setName(s.getName()));

        return serviceResponse;
    }
}
