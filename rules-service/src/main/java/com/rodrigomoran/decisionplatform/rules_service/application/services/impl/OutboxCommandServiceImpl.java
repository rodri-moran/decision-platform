package com.rodrigomoran.decisionplatform.rules_service.application.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodrigomoran.decisionplatform.rules_service.application.services.interfaces.OutboxCommandService;
import com.rodrigomoran.decisionplatform.rules_service.infraestructure.entities.OutboxEvent;
import com.rodrigomoran.decisionplatform.rules_service.infraestructure.persistence.repositories.OutboxEventRepository;
import com.rodrigomoran.decisionplatform.rules_service.interfaces.dtos.RulePublishedEventPayload;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OutboxCommandServiceImpl implements OutboxCommandService {
    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;
    @Override
    @Transactional
    public void enqueueRulePublishedEvent(String ruleKey, Integer versionNumber, String actor, Instant publishedAt, String traceId) {
        this.validateRequiredFields(ruleKey, versionNumber, actor, publishedAt, traceId);
        RulePublishedEventPayload payload = new RulePublishedEventPayload(ruleKey, versionNumber, publishedAt, actor, traceId);
        String payloadJson;

        try {
            payloadJson = objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing RulePublished event payload", e);
        }

        OutboxEvent event = new OutboxEvent();

        event.setEventId(UUID.randomUUID());
        event.setAggregateType("RULE");
        event.setAggregateId(ruleKey);
        event.setEventType("RULE_PUBLISHED");
        event.setPayloadJson(payloadJson);
        event.setStatus(OutboxEvent.Status.PENDING);
        event.setCreatedAt(Instant.now());
        event.setOcurredAt(publishedAt);
        event.setNextAttemptAt(Instant.now());
//        int aux = 2;
//        if(aux > 1){
//            throw new RuntimeException("Forced outbox failure for test");
//        }

        outboxEventRepository.save(event);
    }
    @Override
    @Transactional
    public void markAsSent(Long outboxId, Instant sentAt) {
        if(outboxId == null){
            throw new IllegalArgumentException("OutboxId must not be null");
        }
        OutboxEvent outboxEvent = outboxEventRepository.findById(outboxId)
                .orElseThrow(() -> new EntityNotFoundException("Outbox with id " + outboxId + " not found."));
        outboxEvent.setStatus(OutboxEvent.Status.SENT);
        outboxEvent.setSentAt(sentAt);
        outboxEvent.setLastError(null);
        outboxEventRepository.save(outboxEvent);
    }
    @Override
    @Transactional
    public void markAsFailed(Long outboxId, String errorMessage) {
        if(outboxId == null){
            throw new IllegalArgumentException("OutboxId must not be null");
        }
        OutboxEvent outboxEvent = outboxEventRepository.findById(outboxId)
                .orElseThrow(() -> new EntityNotFoundException("Outbox with id " + outboxId + " not found."));
        outboxEvent.setRetryCount(outboxEvent.getRetryCount() + 1);
        outboxEvent.setLastError(errorMessage);
        outboxEvent.setNextAttemptAt(Instant.now().plusSeconds(30));
        outboxEvent.setStatus(OutboxEvent.Status.FAILED);

        outboxEventRepository.save(outboxEvent);
    }

    private void validateRequiredFields(String ruleKey, Integer versionNumber, String actor, Instant publishedAt, String traceId){
        if(ruleKey == null || ruleKey.isBlank()){
            throw new IllegalArgumentException("RuleKey must not be null or blank");
        }
        if(versionNumber == null){
            throw new IllegalArgumentException("VersionNumber must not be null");
        }
        if (actor == null || actor.isBlank()) {
            throw new IllegalArgumentException("Actor must not be null or blank");
        }
        if (publishedAt == null) {
            throw new IllegalArgumentException("PublishedAt must not be null or blank");
        }
        if (traceId == null || traceId.isBlank()) {
            throw new IllegalArgumentException("TraceId must not be null or blank");
        }
    }
}
