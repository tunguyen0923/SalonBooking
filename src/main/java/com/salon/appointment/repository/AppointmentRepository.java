package com.salon.appointment.repository;

import com.salon.appointment.entity.Appointment;
import com.salon.appointment.entity.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findAllBySalonId(Long salonId);
    Optional<Appointment> findByIdAndSalonId(Long id, Long salonId);

    @Query("SELECT a FROM Appointment a WHERE a.technicianId = :techId " +
           "AND a.status <> :cancelledStatus " +
           "AND a.startTime < :endTime " +
           "AND a.endTime > :startTime")
    List<Appointment> findOverlappingAppointments(
            @Param("techId") Long techId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("cancelledStatus") AppointmentStatus cancelledStatus);
}
