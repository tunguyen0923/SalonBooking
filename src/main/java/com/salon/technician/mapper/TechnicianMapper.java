package com.salon.technician.mapper;

import com.salon.technician.dto.TechnicianRequest;
import com.salon.technician.dto.TechnicianResponse;
import com.salon.technician.entity.Technician;
import org.springframework.stereotype.Component;

@Component
public class TechnicianMapper {

    public Technician toEntity(TechnicianRequest request, Long salonId) {
        Technician technician = new Technician();
        technician.setSalonId(salonId);
        updateEntity(technician, request);
        return technician;
    }

    public void updateEntity(Technician technician, TechnicianRequest request) {
        technician.setName(request.getName());
        technician.setEmail(request.getEmail());
        technician.setPhone(request.getPhone());
    }

    public TechnicianResponse toResponse(Technician technician) {
        TechnicianResponse response = new TechnicianResponse();
        response.setId(technician.getId());
        response.setSalonId(technician.getSalonId());
        response.setName(technician.getName());
        response.setEmail(technician.getEmail());
        response.setPhone(technician.getPhone());
        response.setActive(technician.isActive());
        response.setCreatedAt(technician.getCreatedAt());
        response.setUpdatedAt(technician.getUpdatedAt());
        return response;
    }
}
