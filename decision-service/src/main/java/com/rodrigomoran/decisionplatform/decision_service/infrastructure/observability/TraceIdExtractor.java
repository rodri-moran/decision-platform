package com.rodrigomoran.decisionplatform.decision_service.infrastructure.observability;

import com.rodrigomoran.decisionplatform.decision_service.infrastructure.messaging.dto.RulePublishedEventPayload;

public class TraceIdExtractor {

    public String extractFromEvent(RulePublishedEventPayload event) {
        return event != null ? event.getTraceId() : null;
    }
}
