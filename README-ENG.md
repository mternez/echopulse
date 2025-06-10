# EchoPulse - Distributed Discord Clone

**EchoPulse** is a distributed minimalist clone of Discord designed as a backend technical demonstrator.

## Disclaimer

This project **is not intended for production use**. Its sole purpose is to:

- practice **hexagonal architecture** and clear separation of concerns,
- work with **Kafka**, **Redis**, **WebSocket STOMP**, and **OAuth2** in a distributed context.

No attention has been paid to industrialization, advanced security, or test coverage.

## Front-End Disclaimer

The front-end is intentionally minimal and only serves to illustrate client-side features:

- no focus on UX, code quality, or security;
- no critical logic implemented;
- no maintenance planned.

The core of the project lies entirely in the **distributed backend infrastructure**.

---

## Architecture

EchoPulse is built on an **event-driven hexagonal microservices architecture**, designed to demonstrate the implementation of a **real-time distributed system** using **Kafka** and **WebSocket**.  
The frontend consumes REST APIs or connects via WebSocket.

---

### Overview

Each microservice is independent, with its own database, and communicates asynchronously via **Kafka messages** to ensure loose coupling.  
The frontend interacts with the system through REST and STOMP WebSocket.

    +-----------------+          Kafka          +----------------+
    |  Server Service |  <------------------->  |  Chat Service  |
    |   (REST + JPA)  |                         | (WebSocket + Redis) |
    +--------+--------+                         +--------+-------+
             ^                                             ^
             |                                             |
             | REST (HTTP)                        WebSocket (STOMP)
             |                                             |
      +------+-------+                            +--------+--------+
      |   Front-End   | <-----------------------> |      Client      |
      +--------------+                            +------------------+

---

### Services

#### 1. Server Service

- Creation and management of servers, channels, and memberships
- Kafka event publishing
- Persistence: PostgreSQL
- Communication: REST + Kafka (producer)

#### 2. Chat Service

- Kafka event consumption (channels, messages, users)
- WebSocket STOMP broadcasting
- Volatile message storage in Redis
- Communication: WebSocket + REST (message retrieval)

---

### Authentication

- Provided by Keycloak (OIDC)
- JWT decoded and validated for every request (REST and WebSocket)
- WebSocket authentication integrated in the handshake and STOMP interceptors

---

### Hexagonal Design

- `core/`: pure business logic, framework-independent
- `port in`: interfaces for use cases (input commands)
- `port out`: interfaces for external dependencies (Kafka, Redis, WebSocket)
- `adapters/`: concrete implementations (Kafka producer, WebSocket handler, etc.)

---

### Kafka Events

| Event                  | Sender  | Receiver |
|------------------------|---------|----------|
| channels.create        | server  | chat     |
| channels.delete        | server  | chat     |
| user.join-server       | server  | chat     |
| user.leave-server      | server  | chat     |
| channels.send-message  | chat    | chat     |

---

## Technical Stack

| Domain         | Tools                        |
|----------------|------------------------------|
| Language       | Java 17                      |
| Framework      | Spring Boot 3                |
| Messaging      | Apache Kafka                 |
| Databases      | PostgreSQL (server), Redis (chat) |
| Authentication | Keycloak + Spring Security OAuth2 |
| WebSocket      | STOMP via Spring Messaging   |
| Containers     | Docker + Docker Compose      |
| Architecture   | Modular hexagonal architecture |

---

## Limitations and Areas for Improvement

### Missing

- Automated tests (unit, integration, E2E) across microservices
- Monitoring, structured logging, distributed tracing
- Durable message persistence (currently Redis-only)
- API contracts and documentation (OpenAPI / Swagger)

### Imperfect

- Hardcoded environments and secrets (`localhost`, `admin`, `postgres`)
- Front-end not containerized
- Keycloak integration lacks role/permission mapping
- No role-based authorization enforced in Spring Security
- Redis usage without TTL or cleanup policy
