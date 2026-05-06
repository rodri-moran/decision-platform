# rules-service

## Overview

`rules-service` is responsible for managing the complete lifecycle of business rules.

This service acts as the source of truth for rule definitions, drafts, immutable published versions and rollback operations.

The service allows users or systems to:

- Create rule drafts
- Update rule definitions
- Archive drafts
- Publish immutable versions
- Roll back to previous versions
- Simulate rules before publication

Typical use cases include:

- Fraud detection
- Eligibility validation
- Pricing rules
- Risk scoring
- Workflow routing

---

# Responsibilities

- Manage rule drafts
- Store immutable rule versions
- Handle rollback operations
- Validate rule structures
- Simulate rule evaluation before publication
- Emit rule publication events
- Provide traceability through traceId correlation
- Support idempotent publish operations

---

# Main Flow

```text
Create Draft
      |
      v
Update Draft
      |
      v
Validate / Simulate
      |
      v
Publish Immutable Version
      |
      v
Emit RulePublished Event
      |
      v
decision-service consumes active rule
```

---

# API Documentation

The REST API contract is documented using OpenAPI 3.

## OpenAPI Files

```text
docs/openapi.yml
```

## Swagger Editor

```text
https://editor.swagger.io/
```

## Local Swagger UI

```text
http://localhost:8081/swagger-ui.html
```

## OpenAPI JSON

```text
http://localhost:8081/v3/api-docs
```

---

# Main Endpoints

## Create Draft

```http
POST /draft
```

Creates a new rule draft.

### Example Request

```json
{
  "ruleKey": "fraud-risk-check",
  "name": "Fraud risk check",
  "description": "Detects risky fraud patterns before approving an operation.",
  "definitionJson": {
    "operator": "AND",
    "conditions": [
      {
        "field": "customer.ageDays",
        "operator": "LESS_THAN",
        "value": 30
      }
    ]
  },
  "metadataJson": {
    "category": "fraud",
    "owner": "risk-team"
  }
}
```

---

## Publish Draft

```http
POST /{draftId}/public
```

Publishes a draft as a new immutable rule version.

### Required Headers

```text
X-Actor
X-Trace-Id
X-Idempotency-Key
```

---

## Rollback Version

```http
POST /{ruleKey}/{targetVersionNumber}/rollback
```

Reactivates a previous immutable version.

---

## Simulate Rule

```http
POST /simulation
```

Simulates a rule definition before publication.

### Example Request

```json
{
  "definition": {
    "operator": "AND",
    "conditions": [
      {
        "field": "customer.ageDays",
        "operator": "LESS_THAN",
        "value": 30
      }
    ]
  },
  "sampleContext": {
    "customer": {
      "ageDays": 10
    }
  }
}
```

---

# Architecture Role

Within the platform:

- `rules-service`
    - Source of truth for rule lifecycle and versioning

- `decision-service`
    - Runtime rule evaluation engine

- `audit-service`
    - Decision persistence and traceability

The separation between rule management and rule evaluation is intentional and demonstrates a real event-driven microservice architecture.

---

# Technologies

- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- PostgreSQL
- Flyway
- Kafka
- Docker
- Spring Security OAuth2 Resource Server
- Actuator
- Prometheus
- OpenAPI 3
- Swagger
- MapStruct
- Testcontainers

---

# Observability

The service exposes operational metrics and tracing information through:

- Spring Boot Actuator
- Prometheus metrics
- Structured logs
- traceId propagation

Typical metrics include:

- Draft creation rate
- Publish latency
- Rule publication failures
- Kafka publish failures
- HTTP request latency

The service propagates `X-Trace-Id` across logs and emitted events.

---

# Future Improvements

Potential future enhancements:

- Advanced rule DSL
- Rule validation engine
- Rule approval workflow
- Multi-tenant support
- Rule expiration scheduling
- Redis-based cache invalidation events
- OpenTelemetry distributed tracing
- Bulk rule import/export
- Search indexing integration