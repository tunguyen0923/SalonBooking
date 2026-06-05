# AI System Rules (Spring Boot Backend)

## Purpose

This project is a production-style Spring Boot backend for a salon scheduling system. All code generated (including Copilot suggestions) must follow these rules.

1. Architecture Standard
   - Pattern

     Modular Monolith (NOT microservices yet)

     Each module is self-contained:

     appointment
     customer
     technician
     service
     notification
     auth

     Each module contains:

     controller → service → repository → entity → dto → mapper → event

2. STRICT LAYER RULES
   - Allowed dependencies:
     - controller → service
     - service → repository
     - service → event publisher
     - mapper → dto/entity
   - FORBIDDEN:
     - service → other module repository
     - controller → repository
     - service → notification/email directly
     - cross-domain entity access

   If needed, use EVENTS.

3. BUSINESS LOGIC RULES
   - All business logic MUST live in SERVICE layer.
   - Examples:
     - appointment duration calculation
     - scheduling validation
     - conflict detection
   - NEVER in controller.

4. EVENT-DRIVEN RULE
   - All side effects must use events:
     - Examples: AppointmentCreatedEvent, AppointmentCancelledEvent
   - Flow: Service → Publish Event → Listener → Email/Notification
   - NEVER call email or notification inside service.

5. DATA OWNERSHIP RULE
   - Each module owns its database access.
   - Example: AppointmentService owns AppointmentRepository
   - NO cross-module repository usage.

6. DTO RULE
   - NEVER expose JPA entities in API
   - Always use Request/Response DTOs
   - Use Mapper layer (MapStruct preferred)

7. APPOINTMENT RULES (CORE DOMAIN)
   - No overlapping appointments per technician
   - endTime = sum(service durations)
   - Appointment status is immutable except via service methods

8. ERROR HANDLING
   - Use global exception handling:
     - 400 validation error
     - 404 not found
     - 409 conflict (scheduling overlap)
     - 500 internal error

9. CODING STYLE
   - Use constructor injection (@RequiredArgsConstructor)
   - Use final fields in services
   - No field injection
   - No static business logic

10. LOGGING RULES
    - Log appointment creation, cancellation, rescheduling
    - Never log sensitive data (full email, phone)

11. FUTURE READINESS
    - This system is designed to later extract:
      - notification service
      - scheduling service
      - customer service
    WITHOUT rewriting business logic.

12. GOLDEN RULE
    - If unsure:
      👉 prefer simplicity over abstraction
      👉 prefer explicit code over magic
      👉 prefer events over direct calls

