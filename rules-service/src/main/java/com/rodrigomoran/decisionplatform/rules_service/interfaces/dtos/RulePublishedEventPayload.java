package com.rodrigomoran.decisionplatform.rules_service.interfaces.dtos;
import java.time.Instant;
public record RulePublishedEventPayload(String ruleKey,
                                        Integer version,
                                        Instant publishedAt,
                                        String actor,
                                        String traceId) {
}