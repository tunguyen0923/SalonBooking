# Domain Guide — Salon Scheduling System

## Core Concept

The system revolves around ONE core flow:

Customer → Books Appointment → Technician Assigned → Notification Sent

## Entities

### Customer
May have phone OR email OR both
Identified per salon (multi-tenant)

### Technician
Works for one salon
Has availability schedule (future)

### Service
Has duration + price
Used to calculate appointment end time

### Appointment
Core aggregate root
Cannot overlap for same technician
Has status lifecycle

## Appointment Lifecycle

BOOKED → CONFIRMED → CHECKED_IN → COMPLETED

or

BOOKED → CANCELLED

or

BOOKED → NO_SHOW

## Business Rules

### Scheduling
* No overlapping appointments per technician
* Duration = sum(service durations)
* End time is calculated, not user-provided

### Validation
* Technician must be active
* Customer must exist or be created
* Appointment must be within valid time window

## Event Flow

When appointment is created:

1. AppointmentService creates appointment
2. AppointmentCreatedEvent is published
3. NotificationListener handles:
   - email to customer
   - email to technician

NO direct communication between services.

## Future Extensions (DO NOT IMPLEMENT YET)
* Payments
* Loyalty points
* Technician login
* POS system
* SMS notifications

