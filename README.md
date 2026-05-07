# Decision Platform

Decision Platform is a backend microservices platform for managing versioned business rules and evaluating decisions dynamically without redeploying the application every time a rule changes.

The project was built as a portfolio-oriented backend system focused on microservices, event-driven communication, rule versioning, auditability, API documentation and local infrastructure with Docker.

---

## Overview

In many backend systems, business rules are hardcoded inside the application. This makes every rule change depend on a new deployment.

Decision Platform solves this by separating rule management from rule evaluation:

- Rules can be created, updated and published as versions.
- Published rules are synchronized through events.
- Decisions are evaluated dynamically using the active version of a rule.
- Events are stored for auditability and traceability.

The system is composed of multiple Spring Boot microservices, each one with its own responsibility, database, README and OpenAPI documentation.

---


The platform uses Kafka to avoid direct coupling between services. For example, when a rule is published, rules-service generates an event and the interested services consume it asynchronously.

---

## Microservices

### api-gateway

Entry point of the platform.

## Main responsibilities:

- Centralizes access to the backend services.
- Routes requests to the corresponding microservice.
- Provides a single access point for the system.

**Local port:**
```
http://localhost:8080 
```

---

# rules-service

Service responsible for the rule lifecycle.

## Main responsibilities:

- Create and update rule drafts.
- Archive drafts.
- Publish rules.
- Manage immutable rule versions.
- Roll back to previous versions.
- Simulate rules before publishing.
- Generate rule publication events.
- Persist outbox events for reliable Kafka publishing.
- Handle idempotency for safe publishing retries.

## Main documented operations:

| Operation       | Description                                      |
|----------------|--------------------------------------------------|
| Create draft   | Creates a new editable rule draft                |
| Update draft   | Updates an existing draft                        |
| Archive draft  | Archives a draft                                 |
| Publish draft  | Publishes a draft as a new version               |
| Rollback       | Restores a previous rule version                 |
| Query versions | Lists versions for a rule                        |
| Simulate rule  | Evaluates a rule definition without publishing it|

## Local documentation:
```
rules-service/README.md
```
```
rules-service/docs/openapi.yml
```
```
http://localhost:8081/swagger-ui.html
```


---

# decision-service

Service responsible for evaluating decisions.

## Main responsibilities:

- Consume published rule events from Kafka.
- Keep active rules available for evaluation.
- Evaluate a rule by ruleKey.
- Receive a dynamic input context through data.
- Return whether the rule matched and the evaluation reasons.

## Main endpoint:
```http
POST /decisions/evaluate
```

### Example request
```json
{
  "ruleKey": "new-customer-risk",
  "data": {
    "customer": {
      "ageDays": 3
    },
    "device": {
      "ipReputation": "SAFE"
    }
  }
}
```

### Example response

```json
{
  "ruleKey": "new-customer-risk",
  "result": true,
  "reasons": [
    "Condition matched: field=customer.ageDays, operator=LESS_THAN_OR_EQUALS, expected=7, actual=3"
  ],
  "traceId": "trace-123"
}
```

## Local documentation:
```
decision-service/README.md
```
```
decision-service/docs/openapi.yml
```
```
http://localhost:8082/swagger-ui.html
```

---

# audit-service
Service responsible for storing and exposing audit events.

## Main responsibilities:

- Consume Kafka events.
- Persist audit information.
- Expose audit queries.
- Allow traceability by traceId.
- Allow filtering by event-related fields.

## Local documentation:
```
audit-service/README.md
```
```
audit-service/docs/openapi.yml
```
```
http://localhost:8083/swagger-ui.html
```

# Event-Driven Flow
The main implemented flow is based on rule publication.
1. A rule draft is created in rules-service.
2. The draft is published.
3. rules-service creates a new immutable rule version.
4. An outbox event is persisted.
5. The outbox publisher sends the event to Kafka.
6. decision-service consumes the event and updates its active rule data.
7. audit-service consumes and stores events for traceability.

**Main event currently used by the platform:**
```
RULE_PUBLISHED
```
### Event structure:
```json
{
  "eventId": "uuid",
  "eventType": "RULE_PUBLISHED",
  "ruleKey": "new-customer-risk",
  "version": 1,
  "name": "New customer risk rule",
  "description": "Detects risky operations from new customers",
  "definitionJson": {},
  "metadataJson": {},
  "publishedAt": "2026-05-06T12:00:00Z",
  "publishedBy": "admin",
  "traceId": "trace-123"
}
```

## Rule Model
Rules are defined using a JSON-based structure.

A rule definition contains: 
- An operator, such as AND or OR.
- A list of conditions.
- Optional condition groups.

**Example:**
```json
{
  "operator": "OR",
  "conditions": [
    {
      "field": "customer.ageDays",
      "operator": "LESS_THAN_OR_EQUALS",
      "value": 7
    },
    {
      "field": "device.ipReputation",
      "operator": "EQUALS",
      "value": "RISKY"
    }
  ],
  "groups": []
}
```
This allows the platform to evaluate dynamic input data without changing the backend code.

### API Documentation

Each microservice owns its own API documentation.

| Service | OpenAPI file | Swagger UI |
|---|---|---|
| `rules-service` | `rules-service/docs/openapi.yml` | `http://localhost:8081/swagger-ui.html` |
| `decision-service` | `decision-service/docs/openapi.yml` | `http://localhost:8082/swagger-ui.html` |
| `audit-service` | `audit-service/docs/openapi.yml` | Depends on configured local port |

Each service may also include its own README with more specific technical details.

# Technologies

## Backend
- Java 21
- Spring Boot
- Spring Web MVC
- Spring Validation
- Spring Data JPA
- Hibernate
- MapStruct
- Lombok

## Database
- PostgreSQL

## Messaging
- Apache Kafka
- Spring Kafka
- Outbox Pattern

## Documentation
- OpenAPI 3
- Swagger UI
- Swagger Editor
- springdoc-openapi

## Infrastructure
- Docker
- Docker Compose
- Maven

## Observability
Configured where applicable:
- Spring Boot 
- Actuator
- Micrometer
- Prometheus
- Grafana
- Trace correlation with X-Trace-Id


## Local Setup

### Requirements

To run the complete platform with Docker:

- Docker
- Docker Compose

Java 21 and Maven are required only if you want to build the microservice `.jar` files locally before running Docker.

### Build microservices

Each microservice is an independent Maven project.  
Before starting the Docker environment, build each service to generate its `target` folder and `.jar` file:

```bash
cd api-gateway
mvn clean package -DskipTests
```
```bash
cd ../rules-service
mvn clean package -DskipTests
```
```bash
cd ../decision-service
mvn clean package -DskipTests
```
```bash
cd ../audit-service
mvn clean package -DskipTests
```

### Start the full environment
From the repository root:
```bash
docker compose up -d --build
```

### Stop the environment
```bash
docker compose down
```

### Main local URLs

| Component | URL |
|---|---|
| API Gateway | `http://localhost:8080` |
| rules-service Swagger UI | `http://localhost:8081/swagger-ui.html` |
| decision-service Swagger UI | `http://localhost:8082/swagger-ui.html` |
| Prometheus | `http://localhost:9090`  |
| Grafana | `http://localhost:3000`  |

# Example Use Case
A possible use case is fraud or risk evaluation.

A business team defines a rule such as:

- If the customer is new.
- Or if the device IP reputation is risky.
- Then the operation should be reviewed.

Instead of deploying new backend code, the rule is created as a draft, published as a version and synchronized to the decision service through Kafka.

Then, another system can call decision-service with a dynamic input context and receive an evaluation result.

**Example:**
```bash
{
  "ruleKey": "new-customer-risk",
  "data": {
    "customer": {
      "ageDays": 3
    },
    "device": {
      "ipReputation": "SAFE"
    }
  }
}
```
The response indicates whether the rule matched and includes reasons that explain the evaluation.

# Project Status
### Implemented
- Microservices-based architecture.
- API Gateway.
- Rule draft management.
- Rule publishing.
- Rule versioning.
- Rollback.
- Rule simulation.
- Decision evaluation.
- Kafka-based event synchronization.
- Outbox Pattern in rules-service. 
- Kafka consumption in decision-service.
- Kafka consumption and persistence in audit-service.
- Audit query endpoints.
- Idempotency for publishing.
- PostgreSQL persistence.
- Docker Compose local environment.
- OpenAPI documentation per service.
- Swagger UI for documented services.

### Partially Implemented or Configuration-Dependent
- Observability with Prometheus and Grafana depends on the current Docker Compose and service configuration.
- Exact Swagger UI URL for audit-service depends on its configured local port.
- Testcontainers should only be considered part of the project if present in the current test modules.

# Future Improvements

**Possible improvements:**

- Add OAuth2/OIDC security with Keycloak.
- Add role and scope-based authorization.
- Add Redis cache for active rules in decision-service.
- Add dead-letter topics for Kafka consumers.
- Add more integration tests with Testcontainers.
- Add distributed tracing with OpenTelemetry.
- Add CI pipeline.
- Add notification-service for operational alerts.
- Add richer Grafana dashboards.