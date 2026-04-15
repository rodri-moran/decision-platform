package com.rodrigomoran.decisionplatform.rules_service.application.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodrigomoran.decisionplatform.rules_service.application.services.interfaces.OutboxCommandService;
import com.rodrigomoran.decisionplatform.rules_service.infraestructure.entities.OutboxEvent;
import com.rodrigomoran.decisionplatform.rules_service.infraestructure.entities.RuleVersion;
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
    public void enqueueRulePublishedEvent(RuleVersion ruleVersion, String traceId) {
        System.out.println("Entró al enqueue");
        validateRequiredFields(ruleVersion, traceId);

        RulePublishedEventPayload payload = new RulePublishedEventPayload(
                UUID.randomUUID(),
                "RULE_PUBLISHED",
                ruleVersion.getRuleKey(),
                ruleVersion.getVersionNumber(),
                ruleVersion.getName(),
                ruleVersion.getDescription(),
                ruleVersion.getDefinitionJson(),
                ruleVersion.getMetadataJson(),
                ruleVersion.getPublishedAt(),
                ruleVersion.getPublishedBy(),
                traceId
        );

        String payloadJson;

        try {
            payloadJson = objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException("Error serializing RulePublishedEventPayload", ex);
        }

        OutboxEvent outboxEvent = new OutboxEvent();
        outboxEvent.setEventId(payload.eventId());
        outboxEvent.setAggregateType("RULE");
        outboxEvent.setAggregateId(ruleVersion.getRuleKey());
        outboxEvent.setEventType(payload.eventType());
        outboxEvent.setPayloadJson(payloadJson);
        outboxEvent.setStatus(OutboxEvent.Status.PENDING);
        outboxEvent.setCreatedAt(Instant.now());
        outboxEvent.setOcurredAt(ruleVersion.getPublishedAt());
        outboxEvent.setRetryCount(0);
        outboxEvent.setTraceId(traceId);
        outboxEvent.setNextAttemptAt(Instant.now());
        System.out.println("outboxEvent en enqueue" + outboxEvent);

        outboxEventRepository.save(outboxEvent);
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

    private void validateRequiredFields(RuleVersion ruleVersion, String traceId) {
        if (ruleVersion == null) {
            throw new IllegalArgumentException("ruleVersion cannot be null");
        }
        if (ruleVersion.getRuleKey() == null || ruleVersion.getRuleKey().isBlank()) {
            throw new IllegalArgumentException("ruleKey cannot be null or blank");
        }
        if (ruleVersion.getVersionNumber() == null) {
            throw new IllegalArgumentException("versionNumber cannot be null");
        }
        if (ruleVersion.getDefinitionJson() == null || ruleVersion.getDefinitionJson().isBlank()) {
            throw new IllegalArgumentException("definitionJson cannot be null or blank");
        }
        if (ruleVersion.getPublishedAt() == null) {
            throw new IllegalArgumentException("publishedAt cannot be null");
        }
        if (ruleVersion.getPublishedBy() == null || ruleVersion.getPublishedBy().isBlank()) {
            throw new IllegalArgumentException("publishedBy cannot be null or blank");
        }
        if (traceId == null || traceId.isBlank()) {
            throw new IllegalArgumentException("traceId cannot be null or blank");
        }
    }
}
