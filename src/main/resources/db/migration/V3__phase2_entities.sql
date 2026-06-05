-- V3: Implement Phase 2 Entities (Service, Technician, Customer, Appointment)

-- 1. Service Offering Table
CREATE TABLE service_offering (
    id BIGSERIAL PRIMARY KEY,
    salon_id BIGINT NOT NULL REFERENCES salon(id),
    name VARCHAR(255) NOT NULL,
    description TEXT,
    duration_minutes INTEGER NOT NULL,
    price DECIMAL(19, 2) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE INDEX idx_service_salon_active ON service_offering(salon_id, active);

-- 2. Technician Table
CREATE TABLE technician (
    id BIGSERIAL PRIMARY KEY,
    salon_id BIGINT NOT NULL REFERENCES salon(id),
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    phone VARCHAR(50),
    active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE INDEX idx_technician_salon_active ON technician(salon_id, active);

-- 3. Customer Table
CREATE TABLE customer (
    id BIGSERIAL PRIMARY KEY,
    salon_id BIGINT NOT NULL REFERENCES salon(id),
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    phone VARCHAR(50),
    notes TEXT,
    active BOOLEAN NOT NULL DEFAULT true, -- For soft delete as requested in Module 3
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE INDEX idx_customer_salon ON customer(salon_id);

-- 4. Appointment Table
CREATE TABLE appointment (
    id BIGSERIAL PRIMARY KEY,
    salon_id BIGINT NOT NULL REFERENCES salon(id),
    customer_id BIGINT NOT NULL REFERENCES customer(id),
    technician_id BIGINT NOT NULL REFERENCES technician(id),
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    status VARCHAR(50) NOT NULL,
    notes TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE INDEX idx_appointment_salon_tech_time ON appointment(salon_id, technician_id, start_time, end_time);
CREATE INDEX idx_appointment_salon_customer ON appointment(salon_id, customer_id);

-- 5. Appointment Services (Join Table)
CREATE TABLE appointment_services (
    appointment_id BIGINT NOT NULL REFERENCES appointment(id),
    service_id BIGINT NOT NULL REFERENCES service_offering(id),
    price_snapshot DECIMAL(19, 2) NOT NULL,
    duration_snapshot INTEGER NOT NULL,
    PRIMARY KEY (appointment_id, service_id)
);
