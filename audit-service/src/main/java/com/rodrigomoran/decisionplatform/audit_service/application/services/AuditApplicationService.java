package com.rodrigomoran.decisionplatform.audit_service.application.services;

import com.rodrigomoran.decisionplatform.audit_service.application.dto.*;
import com.rodrigomoran.decisionplatform.audit_service.application.ports.in.GetAuditEventDetailUseCase;
import com.rodrigomoran.decisionplatform.audit_service.application.ports.in.ProcessAuditEventUseCase;
import com.rodrigomoran.decisionplatform.audit_service.application.ports.in.SearchAuditEventsUseCase;
import com.rodrigomoran.decisionplatform.audit_service.application.ports.out.AuditEventPersistencePort;
import com.rodrigomoran.decisionplatform.audit_service.application.ports.out.AuditEventQueryPort;
import com.rodrigomoran.decisionplatform.audit_service.domain.enums.AuditAggregateType;
import com.rodrigomoran.decisionplatform.audit_service.domain.enums.AuditEventType;
import com.rodrigomoran.decisionplatform.audit_service.domain.model.AuditSearchCriteria;
import com.rodrigomoran.decisionplatform.audit_service.infrastructure.messaging.mapper.AuditMessageMapper;
import com.rodrigomoran.decisionplatform.audit_service.infrastructure.observability.AuditMetricsService;

import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AuditApplicationService implements ProcessAuditEventUseCase, SearchAuditEventsUseCase, GetAuditEventDetailUseCase {

    private final AuditEventPersistencePort auditEventPersistencePort;
    private final AuditEventQueryPort auditEventQueryPort;
    private final AuditMessageMapper auditMessageMapper;
    private final AuditMetricsService auditMetricsService;

    @Override
    public void processDecisionMadeEvent(DecisionMadeAuditMessage message) {
        Timer.Sample sample = auditMetricsService.startProcessingTimer();

        try {
            String eventType = AuditEventType.DECISION_MADE.name();

            if (auditEventPersistencePort.existsByEventId(message.getEventId())) {
                auditMetricsService.incrementAuditEventDuplicated(eventType);
                return;
            }

            var entity = auditMessageMapper.toEntity(message);

            auditEventPersistencePort.save(entity);

            auditMetricsService.incrementAuditEventProcessed(eventType);
        } finally {
            auditMetricsService.stopProcessingTimer(sample, AuditEventType.DECISION_MADE.name());
        }
    }

    @Override
    public void processRulePublishedEvent(RulePublishedAuditMessage message) {
        Timer.Sample sample = auditMetricsService.startProcessingTimer();

        try {
            String eventType = AuditEventType.RULE_PUBLISHED.name();

            if (auditEventPersistencePort.existsByEventId(message.getEventId())) {
                auditMetricsService.incrementAuditEventDuplicated(eventType);
                return;
            }

            var entity = auditMessageMapper.toEntity(message);

            auditEventPersistencePort.save(entity);

            auditMetricsService.incrementAuditEventProcessed(eventType);
        } finally {
            auditMetricsService.stopProcessingTimer(sample, AuditEventType.RULE_PUBLISHED.name());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AuditResponse> search(AuditSearchRequest request) {
        AuditSearchCriteria criteria = buildCriteria(request);

        return auditEventQueryPort
                .search(criteria)
                .map(auditMessageMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public AuditDetailResponse getById(UUID id) {
        var entity = auditEventQueryPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Audit event not found with id: " + id));

        return auditMessageMapper.toDetailResponse(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditResponse> getByTraceId(String traceId) {
        return auditMessageMapper.toResponseList(
                auditEventQueryPort.findByTraceId(traceId)
        );
    }

    private AuditSearchCriteria buildCriteria(AuditSearchRequest request) {
        return AuditSearchCriteria.builder()
                .eventType(parseEventType(request.getEventType()))
                .aggregateType(parseAggregateType(request.getAggregateType()))
                .aggregateId(request.getAggregateId())
                .traceId(request.getTraceId())
                .decision(request.getDecision())
                .fromTimestamp(request.getFromTimestamp())
                .toTimestamp(request.getToTimestamp())
                .page(request.getPage())
                .size(request.getSize())
                .sortBy(request.getSortBy())
                .sortDirection(request.getSortDirection())
                .build();
    }

    private AuditEventType parseEventType(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return AuditEventType.valueOf(value.toUpperCase());
    }

    private AuditAggregateType parseAggregateType(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return AuditAggregateType.valueOf(value.toUpperCase());
    }
}