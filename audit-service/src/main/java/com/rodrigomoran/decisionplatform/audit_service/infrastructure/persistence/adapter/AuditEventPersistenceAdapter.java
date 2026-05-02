package com.rodrigomoran.decisionplatform.audit_service.infrastructure.persistence.adapter;

import com.rodrigomoran.decisionplatform.audit_service.application.ports.out.AuditEventPersistencePort;
import com.rodrigomoran.decisionplatform.audit_service.application.ports.out.AuditEventQueryPort;
import com.rodrigomoran.decisionplatform.audit_service.domain.model.AuditSearchCriteria;
import com.rodrigomoran.decisionplatform.audit_service.infrastructure.persistence.entity.AuditEventEntity;
import com.rodrigomoran.decisionplatform.audit_service.infrastructure.persistence.repository.AuditEventJpaRepository;
import com.rodrigomoran.decisionplatform.audit_service.infrastructure.persistence.repository.AuditEventSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AuditEventPersistenceAdapter implements AuditEventPersistencePort, AuditEventQueryPort {

    private final AuditEventJpaRepository auditEventJpaRepository;

    @Override
    public boolean existsByEventId(String eventId) {
        return auditEventJpaRepository.existsByEventId(eventId);
    }

    @Override
    public AuditEventEntity save(AuditEventEntity entity) {
        return auditEventJpaRepository.save(entity);
    }

    @Override
    public Optional<AuditEventEntity> findById(UUID id) {
        return auditEventJpaRepository.findById(id);
    }

    @Override
    public List<AuditEventEntity> findByTraceId(String traceId) {
        return auditEventJpaRepository.findByTraceIdOrderByOccurredAtAsc(traceId);
    }

    @Override
    public Page<AuditEventEntity> search(AuditSearchCriteria criteria) {
        Pageable pageable = buildPageable(criteria);

        return auditEventJpaRepository.findAll(
                AuditEventSpecification.byCriteria(criteria),
                pageable
        );
    }

    private Pageable buildPageable(AuditSearchCriteria criteria) {
        int page = criteria.getPage() != null ? criteria.getPage() : 0;
        int size = criteria.getSize() != null ? criteria.getSize() : 20;

        String sortBy = criteria.getSortBy() != null && !criteria.getSortBy().isBlank()
                ? criteria.getSortBy()
                : "occurredAt";

        Sort.Direction direction = "ASC".equalsIgnoreCase(criteria.getSortDirection())
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        return PageRequest.of(page, size, Sort.by(direction, sortBy));
    }
}
