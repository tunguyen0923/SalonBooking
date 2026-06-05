package com.salon.technician.service;

import com.salon.common.exception.NotFoundException;
import com.salon.common.security.SecurityUtil;
import com.salon.technician.dto.TechnicianRequest;
import com.salon.technician.dto.TechnicianResponse;
import com.salon.technician.entity.Technician;
import com.salon.technician.mapper.TechnicianMapper;
import com.salon.technician.repository.TechnicianRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TechnicianService {

    private final TechnicianRepository repository;
    private final TechnicianMapper mapper;

    public TechnicianService(TechnicianRepository repository, TechnicianMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional
    public TechnicianResponse createTechnician(TechnicianRequest request) {
        Long salonId = SecurityUtil.getCurrentSalonId();
        Technician technician = mapper.toEntity(request, salonId);
        return mapper.toResponse(repository.save(technician));
    }

    @Transactional(readOnly = true)
    public List<TechnicianResponse> getAllTechnicians() {
        Long salonId = SecurityUtil.getCurrentSalonId();
        return repository.findAllBySalonIdAndActiveTrue(salonId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TechnicianResponse getTechnicianById(Long id) {
        return mapper.toResponse(getByIdForCurrentSalon(id));
    }

    @Transactional
    public TechnicianResponse updateTechnician(Long id, TechnicianRequest request) {
        Technician technician = getByIdForCurrentSalon(id);
        mapper.updateEntity(technician, request);
        return mapper.toResponse(repository.save(technician));
    }

    @Transactional
    public TechnicianResponse deleteTechnician(Long id) {
        Technician technician = getByIdForCurrentSalon(id);
        technician.setActive(false);
        return mapper.toResponse(repository.save(technician));
    }

    private Technician getByIdForCurrentSalon(Long id) {
        Long salonId = SecurityUtil.getCurrentSalonId();
        return repository.findByIdAndSalonIdAndActiveTrue(id, salonId)
                .orElseThrow(() -> new NotFoundException("Technician not found with id: " + id));
    }
}
