# Crafting a Self‑Documenting Modular Monolith with DDD Principles

**Speakers:**

- Gregorio Palamà – adesso
- Mattia Ciriolo – adesso

---

## Abstract

Struggling with monoliths that turn into unmanageable beasts? This talk explores the **modular monolith** architecture
as a compelling alternative to microservices, balancing maintainability with operational simplicity. Learn how to
leverage **Spring Modulith** and **JMolecules** to create self‑documenting code that clearly expresses Domain‑Driven
Design (DDD) principles. We will also see how to implement event‑driven architecture and distributed
transactions—exploiting their advantages such as high cohesion and low coupling—while sidestepping many of the
complexities associated with microservices patterns like compensation actions, transactional outbox, message relay, and
dead letter queues.

---

## Repository Structure

spring-io-conf-25\
├── frontend\
│ └── spring-io-conf # SvelteKit frontend for the demo UI\
└── src # Spring Boot Modulith + JMolecules backend

## Getting Started

```bash
# Verify Java
java -version

# Verify Maven
mvn -v

# Verify Node.js and npm
node -v
npm -v

mvn clean spring-boot:run

cd frontend/spring-io-conf && npm install && npm run dev

```

