package com.rodrigomoran.decisionplatform.decision_service.infrastructure.messaging.event;

import lombok.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DecisionMadeEventPayload {

    private UUID eventId;
    private String eventType;

    private String decisionId;
    private String decision;

    private String ruleKey;
    private Integer ruleVersion;
    private String matchedRule;

    private List<String> reasons;

    private Instant occurredAt;
    private String inputRedactedJson;

    private Long latencyMs;
    private String traceId;

    private String aggregateType;
    private String aggregateId;
}