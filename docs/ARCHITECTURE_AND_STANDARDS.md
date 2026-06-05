# Architecture & Coding Standards

## Architecture Style

* Modular Monolith (Spring Boot)
* Domain-driven modules
* Event-driven internal communication

---

## Package Structure

Each module MUST follow:

appointment/
├── controller
├── service
├── repository
├── entity
├── dto
├── mapper
└── event

---

## Dependency Rules

Allowed:

* controller → service
* service → repository
* service → event publisher

NOT allowed:

* service → other module repository
* controller → repository
* cross-domain entity access

---

## Transaction Rules

* Each service method must define transaction boundaries
* Use @Transactional at service layer only

---

## Event-Driven Design

Use Spring Application Events:

* AppointmentCreatedEvent
* AppointmentCancelledEvent

All side effects go through listeners.

---

## Deletion Policy

* ALL deletions are SOFT-DELETES.
* Use the `DELETE` HTTP method for logical removal.
* Mark records as `active = false`.
* GET APIs must exclude inactive records by default.
* Provide documentation in Swagger/OpenAPI to clarify this behavior to clients.

---

## DTO Rules

* Never expose JPA entities in API responses
* Always use DTOs for request/response
* Use MapStruct for mapping

---

## Error Handling

Standard API response:

* 400 validation error
* 404 not found
* 409 conflict (double booking)
* 500 internal error

Use global exception handler.

---

## Logging Rules

* Log all appointment creation attempts
* Do NOT log sensitive data (emails only partially masked)

---

## Naming Conventions

* Service classes: *Service
* Controllers: *Controller
* Events: *Event
* DTOs: *Request / *Response suffix

---

## Future Extension Ready Design

System must be ready for:

* Microservice extraction (notification first candidate)
* Kafka/RabbitMQ integration
* POS integration
* Technician login system

