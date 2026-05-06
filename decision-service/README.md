# decision-service

## Overview

`decision-service` is responsible for evaluating business rules in real time.

This service receives a `ruleKey` and a dynamic input context, executes the active rule associated with that key, and returns an explainable decision result.

Unlike `rules-service`, this microservice does not manage drafts, versions or publishing. Its responsibility is focused on runtime evaluation.

The service is designed to answer questions such as:

- Does this customer match the rule?
- Should this operation be approved or reviewed?
- Which conditions matched or failed?
- Which rule version was used during evaluation?

Typical use cases include:

- Fraud detection
- Eligibility validation
- Pricing decisions
- Risk scoring
- Workflow routing

---

## Responsibilities

- Evaluate active business rules
- Receive dynamic JSON input data
- Convert REST requests into an internal evaluation context
- Execute the decision evaluation use case
- Return an explainable decision result
- Include the evaluated rule key and version in the response
- Provide traceability through `traceId`
- Keep rule evaluation separated from rule management

---

## Main Flow

```text
Client sends evaluation request
        |
        v
POST /decisions/evaluate
        |
        v
Map request DTO to EvaluationContext
        |
        v
Execute EvaluateDecisionUseCase
        |
        v
Evaluate active rule by ruleKey
        |
        v
Map DecisionResult to response DTO
        |
        v
Return DecisionResponseDTO
```

---

## API Documentation

The REST API contract is documented using OpenAPI 3.

- OpenAPI file: `docs/openapi.yml`
- Swagger Editor: `https://editor.swagger.io/`
- Local Swagger UI: `http://localhost:8082/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8082/v3/api-docs`

---

## Main Endpoints

### Evaluate Decision

```http
POST /decisions/evaluate
```

Evaluates the active rule associated with the provided `ruleKey`.

#### Request Body

```json
{
  "ruleKey": "fraud-risk-check",
  "data": {
    "customer": {
      "ageDays": 10
    },
    "device": {
      "ipReputation": "RISKY"
    }
  }
}
```

#### Request Fields

| Field | Type | Required | Description |
|---|---|---:|---|
| `ruleKey` | `string` | Yes | Unique key of the rule to evaluate. |
| `data` | `object` | No | Dynamic JSON context used during rule evaluation. |

#### Response Body

```json
{
  "result": true,
  "reasons": [
    "Condition matched: customer.ageDays LESS_THAN 30",
    "Condition matched: device.ipReputation EQUALS RISKY"
  ],
  "ruleKey": "fraud-risk-check",
  "version": 3,
  "traceId": "550e8400-e29b-41d4-a716-446655440000",
  "evaluatedAt": "2026-05-06T20:10:00Z"
}
```

#### Response Fields

| Field | Type | Description |
|---|---|---|
| `result` | `boolean` | Indicates whether the rule matched the provided input data. |
| `reasons` | `array<string>` | Human-readable explanations of the evaluation result. |
| `ruleKey` | `string` | Rule key used during evaluation. |
| `version` | `integer` | Active rule version evaluated by the service. |
| `traceId` | `string` | Correlation ID used for tracing and observability. |
| `evaluatedAt` | `datetime` | Timestamp when the decision was evaluated. |

---

## Architecture Role

Within the platform:

- `rules-service`
    - Manages rule drafts, publication, versioning and rollback.

- `decision-service`
    - Evaluates active rules at runtime.

- `audit-service`
    - Stores decision results and provides traceability.

The separation between `rules-service` and `decision-service` is intentional.

`rules-service` owns the lifecycle of a rule.

`decision-service` owns the execution of a rule.

This allows rules to be changed and published without redeploying the decision evaluation logic.

---

## Technologies

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

## Observability

The service exposes operational information through:

- Spring Boot Actuator
- Prometheus metrics
- Structured logs
- `traceId` propagation

Typical metrics include:

- Decision evaluation latency
- Number of evaluated decisions
- Evaluation success rate
- Evaluation error rate
- Active rule lookup latency
- HTTP request latency

The `traceId` allows tracking a decision evaluation across services, logs and events.

---

## Testing Strategy

The project uses:

- Unit tests
- Integration tests
- Testcontainers

Typical unit test scenarios include:

- Mapping `EvaluateDecisionRequestDTO` to `EvaluationContext`
- Evaluating a rule that matches the input data
- Evaluating a rule that does not match the input data
- Returning explanation reasons correctly

Typical integration test scenarios include:

- Evaluating an existing active rule
- Returning `404` or domain error when the rule key does not exist
- Verifying the response contains `ruleKey`, `version`, `traceId` and `evaluatedAt`
- Validating rule evaluation with nested JSON input data

Infrastructure that can be started with Testcontainers:

- PostgreSQL
- Kafka
- Redis, if rule caching is enabled

---

## Future Improvements

Potential future enhancements:

- Redis-based active rule cache
- Rule cache invalidation through Kafka events
- Bulk decision evaluation endpoint
- DecisionMade event emission
- OpenTelemetry distributed tracing
- More detailed evaluation explanations
- Rule evaluation metrics by `ruleKey`
- Async evaluation pipeline
- Circuit breaker for external dependencies