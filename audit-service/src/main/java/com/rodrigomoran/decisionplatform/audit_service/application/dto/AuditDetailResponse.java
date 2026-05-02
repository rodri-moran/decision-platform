package com.rodrigomoran.decisionplatform.audit_service.application.dto;

import lombok.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditDetailResponse {
    private UUID id;
    private String eventId;
    private String eventType;
    private String aggregateType;
    private String aggregateId;
    private String traceId;
    private Instant occurredAt;
    private String decision;
    private String matchedRule;
    private List<String> reasons;
    private String inputRedactedJson;
    private Long latencyMs;
    private String payloadJson;
    private Instant createdAt;
}