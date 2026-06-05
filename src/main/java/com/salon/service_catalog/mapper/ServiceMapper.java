package com.salon.service_catalog.mapper;

import com.salon.service_catalog.dto.ServiceRequest;
import com.salon.service_catalog.dto.ServiceResponse;
import com.salon.service_catalog.entity.ServiceOffering;
import org.springframework.stereotype.Component;

@Component
public class ServiceMapper {

    public ServiceOffering toEntity(ServiceRequest request, Long salonId) {
        ServiceOffering service = new ServiceOffering();
        service.setSalonId(salonId);
        updateEntity(service, request);
        return service;
    }

    public void updateEntity(ServiceOffering service, ServiceRequest request) {
        service.setName(request.getName());
        service.setDescription(request.getDescription());
        service.setDurationMinutes(request.getDurationMinutes());
        service.setPrice(request.getPrice());
    }

    public ServiceResponse toResponse(ServiceOffering service) {
        ServiceResponse response = new ServiceResponse();
        response.setId(service.getId());
        response.setSalonId(service.getSalonId());
        response.setName(service.getName());
        response.setDescription(service.getDescription());
        response.setDurationMinutes(service.getDurationMinutes());
        response.setPrice(service.getPrice());
        response.setActive(service.isActive());
        response.setCreatedAt(service.getCreatedAt());
        response.setUpdatedAt(service.getUpdatedAt());
        return response;
    }
}
