package com.rodrigomoran.decisionplatform.rules_service.application.services.interfaces;

import jakarta.transaction.Transactional;

import java.time.Instant;

public interface OutboxCommandService {

    @Transactional
    void enqueueRulePublishedEvent(String ruleKey, Integer versionNumber, String actor, Instant publishedAt, String traceId);

    @Transactional
    void markAsSent(Long outboxId, Instant sentAt);

    @Transactional
    void markAsFailed(Long outboxId, String errorMessage);
}