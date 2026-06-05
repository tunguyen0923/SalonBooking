# SalonBooking — Backend

This repository is a multi-tenant SaaS backend for nail salons, built with Spring Boot and PostgreSQL.

## Documentation

Developer references and architecture details:

- `docs/ARCHITECTURE_AND_STANDARDS.md` — Core architecture, coding standards, and deletion policy.
- `docs/DOMAIN_MODEL.md` — Domain model and core entities.
- `docs/CODE_PATTERNS.md` — Implementation patterns for controllers and services.
- `docs/AI_SYSTEM_RULES.md` — Guidelines for AI-assisted development.

## Getting Started

### Prerequisites
- Java 21
- Docker & Docker Compose
- Maven

### Running Locally

1. **Start Infrastructure**:
   ```bash
   docker compose up -d
   ```

2. **Run Application**:
   ```bash
   mvn spring-boot:run
   ```

3. **API Documentation**:
   Access Swagger UI at [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## Project Status

### ✅ Phase 1: Foundation (Complete)
- **Authentication**: JWT-based auth with HS256 and BCrypt password hashing.
- **Multi-Tenancy**: Architecture designed for `salonId` isolation from the start.
- **Security**: Hardened filter chain, typed `UserPrincipal`, and `SecurityUtil` for context extraction.
- **Infrastructure**: Flyway migrations, PostgreSQL integration, and Global Exception Handling.

### ✅ Phase 2: Core Scheduling (Complete)
- **Service Catalog**: Management of salon services with duration and price rules.
- **Technician Management**: Technician records with active/inactive status.
- **Customer Management**: CRM for salon clients with contact validation.
- **Appointment Engine**:
    - **Automated Scheduling**: System-calculated `endTime` based on service durations.
    - **Double-Booking Prevention**: Conflict detection for technicians (409 Conflict).
    - **Historical Integrity**: Snapshot of price and duration at the time of booking.
- **Soft-Delete Policy**: Implementation of `active` flags across all modules to preserve data integrity.

## Architecture Highlights

- **Modular Monolith**: Domain-based package structure (`service_catalog`, `technician`, `appointment`, etc.).
- **Strict Layering**: Controller → Service → Repository flow.
- **DTO-Only API**: No JPA entities are exposed through the REST interface.
- **Tenant Isolation**: `salonId` is derived strictly from JWT claims; cross-tenant access is prevented at the service layer.
- **Flyway Migrations**: Versioned schema updates for predictable deployments.

## Testing

A Postman collection is provided in the root directory:
- `SalonBooking.postman_collection.json`
- `SalonBooking.postman_environment.json`

Import both into Postman and select the `SalonBooking-Local` environment to begin testing.
