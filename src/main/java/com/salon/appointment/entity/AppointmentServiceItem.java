package com.salon.appointment.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "appointment_services")
public class AppointmentServiceItem {

    @EmbeddedId
    private AppointmentServiceId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("appointmentId")
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @Column(name = "price_snapshot", nullable = false)
    private BigDecimal priceSnapshot;

    @Column(name = "duration_snapshot", nullable = false)
    private Integer durationSnapshot;

    public AppointmentServiceItem() {}

    public AppointmentServiceItem(Appointment appointment, Long serviceId, BigDecimal price, Integer duration) {
        this.appointment = appointment;
        this.id = new AppointmentServiceId(appointment.getId(), serviceId);
        this.priceSnapshot = price;
        this.durationSnapshot = duration;
    }

    public AppointmentServiceId getId() {
        return id;
    }

    public void setId(AppointmentServiceId id) {
        this.id = id;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public BigDecimal getPriceSnapshot() {
        return priceSnapshot;
    }

    public void setPriceSnapshot(BigDecimal priceSnapshot) {
        this.priceSnapshot = priceSnapshot;
    }

    public Integer getDurationSnapshot() {
        return durationSnapshot;
    }

    public void setDurationSnapshot(Integer durationSnapshot) {
        this.durationSnapshot = durationSnapshot;
    }

    @Embeddable
    public static class AppointmentServiceId implements Serializable {
        private Long appointmentId;
        private Long serviceId;

        public AppointmentServiceId() {}

        public AppointmentServiceId(Long appointmentId, Long serviceId) {
            this.appointmentId = appointmentId;
            this.serviceId = serviceId;
        }

        public Long getAppointmentId() {
            return appointmentId;
        }

        public void setAppointmentId(Long appointmentId) {
            this.appointmentId = appointmentId;
        }

        public Long getServiceId() {
            return serviceId;
        }

        public void setServiceId(Long serviceId) {
            this.serviceId = serviceId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            AppointmentServiceId that = (AppointmentServiceId) o;
            return Objects.equals(appointmentId, that.appointmentId) && Objects.equals(serviceId, that.serviceId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(appointmentId, serviceId);
        }
    }
}
