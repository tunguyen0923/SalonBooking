# Copilot Development Guide — Salon Scheduling Backend

## Purpose

This repository is a **Spring Boot backend for a nail salon scheduling system**. The system supports multi-tenant salons with appointment booking, technicians, services, and email notifications.

## Core Rule

Copilot must follow the architecture and domain boundaries defined in this repository. Do NOT introduce random patterns or cross-module dependencies.

---

## Project Goal (MVP)

* Salon can manage services and technicians
* Customers can book appointments
* System prevents double-booking
* System sends email notifications
* Appointments are the core business entity

---

## Architecture Style

* Modular Monolith (NOT microservices yet)
* Domain-based modules
* Event-driven inside monolith

Modules:

* auth
* salon
* customer
* technician
* service
* appointment
* notification
* common

---

## Forbidden Actions

* Do NOT implement microservices
* Do NOT directly send emails inside business logic
* Do NOT access other module repositories directly
* Do NOT mix domain logic between modules

---

## Required Pattern

Business flow MUST follow:

1. Controller receives request
2. Service handles business logic
3. Domain event is published
4. Notification module reacts to event

Example:
AppointmentCreatedEvent → NotificationListener → EmailService

---

## Event Rules

Always use events for side effects:

* AppointmentCreatedEvent
* AppointmentCancelledEvent
* AppointmentRescheduledEvent

---

## Data Ownership Rule

Each module owns its own data:

* CustomerService owns Customer
* AppointmentService owns Appointment
* TechnicianService owns Technician

No cross-repository updates between modules.

---

## Email Rule

Email must ONLY be sent from:

* Notification module
* Event listeners

NEVER inside:

* AppointmentService
* CustomerService

