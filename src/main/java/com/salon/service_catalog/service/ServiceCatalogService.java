package com.salon.service_catalog.service;

import com.salon.common.exception.NotFoundException;
import com.salon.common.security.SecurityUtil;
import com.salon.service_catalog.dto.ServiceRequest;
import com.salon.service_catalog.dto.ServiceResponse;
import com.salon.service_catalog.entity.ServiceOffering;
import com.salon.service_catalog.mapper.ServiceMapper;
import com.salon.service_catalog.repository.ServiceOfferingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceCatalogService {

    private final ServiceOfferingRepository repository;
    private final ServiceMapper mapper;

    public ServiceCatalogService(ServiceOfferingRepository repository, ServiceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional
    public ServiceResponse createService(ServiceRequest request) {
        Long salonId = SecurityUtil.getCurrentSalonId();
        ServiceOffering service = mapper.toEntity(request, salonId);
        return mapper.toResponse(repository.save(service));
    }

    @Transactional(readOnly = true)
    public List<ServiceResponse> getAllServices() {
        Long salonId = SecurityUtil.getCurrentSalonId();
        return repository.findAllBySalonIdAndActiveTrue(salonId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ServiceResponse getServiceById(Long id) {
        return mapper.toResponse(getByIdForCurrentSalon(id));
    }

    @Transactional
    public ServiceResponse updateService(Long id, ServiceRequest request) {
        ServiceOffering service = getByIdForCurrentSalon(id);
        mapper.updateEntity(service, request);
        return mapper.toResponse(repository.save(service));
    }

    @Transactional
    public ServiceResponse deleteService(Long id) {
        ServiceOffering service = getByIdForCurrentSalon(id);
        service.setActive(false);
        return mapper.toResponse(repository.save(service));
    }

    private ServiceOffering getByIdForCurrentSalon(Long id) {
        Long salonId = SecurityUtil.getCurrentSalonId();
        return repository.findByIdAndSalonIdAndActiveTrue(id, salonId)
                .orElseThrow(() -> new NotFoundException("Service not found with id: " + id));
    }
}
