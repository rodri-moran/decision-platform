package com.rodrigomoran.decisionplatform.decision_service.infrastructure.messaging.dto;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;
@Data
public class RulePublishedEventPayload {

    private UUID eventId;
    private String eventType;
    private String ruleKey;
    private Integer version;
    private String name;
    private String description;
    private String definitionJson;
    private String metadataJson;
    private Instant publishedAt;
    private String publishedBy;
    private String traceId;
}

