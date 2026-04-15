package com.rodrigomoran.decisionplatform.rules_service.application.services.interfaces;

import com.rodrigomoran.decisionplatform.rules_service.infraestructure.entities.RuleVersion;
import jakarta.transaction.Transactional;

import java.time.Instant;

public interface OutboxCommandService {

    @Transactional
    void enqueueRulePublishedEvent(RuleVersion ruleVersion, String traceId);

    @Transactional
    void markAsSent(Long outboxId, Instant sentAt);

    @Transactional
    void markAsFailed(Long outboxId, String errorMessage);
}