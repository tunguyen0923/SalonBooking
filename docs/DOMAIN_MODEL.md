# Domain Model — Salon Scheduling System

## Core Entities

---

## Customer

Represents a salon customer.

Fields:

* id
* salonId
* name
* phone (optional)
* email (optional)
* notes
* createdAt

Rule:

* At least one contact method required (phone or email)

---

## Technician

Represents a nail technician.

Fields:

* id
* salonId
* name
* phone (optional)
* email (optional)
* active

Future:

* specialty tags
* login account (later phase)

---

## Service

A service offered by the salon.

Fields:

* id
* salonId
* name
* description
* durationMinutes
* price
* active

---

## Appointment

Core scheduling entity.

Fields:

* id
* salonId
* customerId
* technicianId
* startTime
* endTime
* status
* notes

Status:

* BOOKED
* CONFIRMED
* CHECKED_IN
* COMPLETED
* CANCELLED
* NO_SHOW

---

## AppointmentService (Join Table)

Represents services inside an appointment.

Fields:

* appointmentId
* serviceId
* priceSnapshot
* durationSnapshot

Rule:

* Store snapshot values to prevent historical data corruption

---

## Business Rules

### Scheduling Rules

* No overlapping appointments per technician
* Appointment end time = sum(service durations)
* Technician must be active
* Appointment must be within working hours (future enhancement)

---

## Check-In Flow

* Customer checks in via email link or identifier
* Appointment status → CHECKED_IN
* Notification sent to technician

