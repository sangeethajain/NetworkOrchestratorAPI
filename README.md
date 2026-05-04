# Network Orchestrator API

A Spring Boot-based backend system that simulates **network device orchestration and inventory management**, inspired by real-world telecom systems.

## Project Overview

This project allows managing network devices (routers, switches) across different locations such as offices and data centers.

It simulates real-world operations like:

* Device creation and management
* Filtering by location
* Restarting network devices
* Status transitions (ACTIVE → RESTARTING → ACTIVE)

---

## Tech Stack

* Java 17
* Spring Boot
* Spring Data JPA
* H2 Database (in-memory)
* JUnit 5
* Mockito
* MockMvc

---

## Features

### Device Management

* Create device
* Get all devices
* Get device by ID
* Delete device

### Filtering

* Get devices by location (office/datacenter)

### Network Simulation

* Restart device (simulates status change)

---

## Testing

The project includes:

* Unit Tests (Service layer using Mockito)
* Controller Tests (MockMvc)
* Integration Tests (end-to-end API flow)

---

## How to Run

mvn spring-boot:run


---

## API Endpoints

| Method | Endpoint                     | Description        |
| ------ | ---------------------------- | ------------------ |
| POST   | /devices                     | Create device      |
| GET    | /devices                     | Get all devices    |
| GET    | /devices/{id}                | Get device by ID   |
| DELETE | /devices/{id}                | Delete device      |
| POST   | /devices/{id}/restart        | Restart device     |
| GET    | /devices/location/{location} | Filter by location |

---

## Key Learnings

* REST API design using Spring Boot
* Test automation (unit + integration)
* Exception handling with proper HTTP status codes
* Layered architecture (Controller → Service → Repository)

---

## Future Improvements

* Swagger API documentation
  

---
