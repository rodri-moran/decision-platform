package com.rodrigomoran.decisionplatform.rules_service.interfaces.dtos;
import java.time.Instant;
import java.util.UUID;

public record RulePublishedEventPayload(
        UUID eventId,
        String eventType,
        String ruleKey,
        Integer version,
        String name,
        String description,
        String definitionJson,
        String metadataJson,
        Instant publishedAt,
        String publishedBy,
        String traceId
) {
}