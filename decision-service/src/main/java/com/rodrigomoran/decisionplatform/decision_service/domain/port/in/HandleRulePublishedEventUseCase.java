package com.rodrigomoran.decisionplatform.decision_service.domain.port.in;

import com.rodrigomoran.decisionplatform.decision_service.infrastructure.messaging.dto.RulePublishedEventPayload;
import org.springframework.stereotype.Service;

@Service
public interface HandleRulePublishedEventUseCase {
    void handle(RulePublishedEventPayload event);
}
