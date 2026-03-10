package com.rodrigomoran.decisionplatform.rules_service.application.services.impl;

import com.rodrigomoran.decisionplatform.rules_service.application.services.interfaces.OutboxCommandService;
import jakarta.transaction.Transactional;

import java.time.Instant;

public class OutboxCommandServiceImpl implements OutboxCommandService {
    @Override
    @Transactional
    public void enqueueRulePublishedEvent(String ruleKey, Integer versionNumber, String actor, Instant publishedAt, String traceId) {

    }
    @Override
    @Transactional
    public void markAsSent(Long outboxId, Instant sentAt) {

    }
    @Override
    @Transactional
    public void markAsFailed(Long outboxId, String errorMessage) {

    }
}
