package com.rodrigomoran.decisionplatform.audit_service.infrastructure.persistence.repository;

import com.rodrigomoran.decisionplatform.audit_service.domain.model.AuditSearchCriteria;
import com.rodrigomoran.decisionplatform.audit_service.infrastructure.persistence.entity.AuditEventEntity;
import org.springframework.data.jpa.domain.Specification;

public final class AuditEventSpecification {

    private AuditEventSpecification() {
    }

    public static Specification<AuditEventEntity> byCriteria(AuditSearchCriteria criteria) {
        return (root, query, criteriaBuilder) -> {
            var predicates = criteriaBuilder.conjunction();

            if (criteria == null) {
                return predicates;
            }

            if (criteria.getEventType() != null) {
                predicates = criteriaBuilder.and(
                        predicates,
                        criteriaBuilder.equal(root.get("eventType"), criteria.getEventType())
                );
            }

            if (criteria.getAggregateType() != null) {
                predicates = criteriaBuilder.and(
                        predicates,
                        criteriaBuilder.equal(root.get("aggregateType"), criteria.getAggregateType())
                );
            }

            if (criteria.getAggregateId() != null && !criteria.getAggregateId().isBlank()) {
                predicates = criteriaBuilder.and(
                        predicates,
                        criteriaBuilder.equal(root.get("aggregateId"), criteria.getAggregateId())
                );
            }

            if (criteria.getTraceId() != null && !criteria.getTraceId().isBlank()) {
                predicates = criteriaBuilder.and(
                        predicates,
                        criteriaBuilder.equal(root.get("traceId"), criteria.getTraceId())
                );
            }

            if (criteria.getDecision() != null && !criteria.getDecision().isBlank()) {
                predicates = criteriaBuilder.and(
                        predicates,
                        criteriaBuilder.equal(root.get("decision"), criteria.getDecision())
                );
            }

            if (criteria.getFromTimestamp() != null) {
                predicates = criteriaBuilder.and(
                        predicates,
                        criteriaBuilder.greaterThanOrEqualTo(root.get("occurredAt"), criteria.getFromTimestamp())
                );
            }

            if (criteria.getToTimestamp() != null) {
                predicates = criteriaBuilder.and(
                        predicates,
                        criteriaBuilder.lessThanOrEqualTo(root.get("occurredAt"), criteria.getToTimestamp())
                );
            }

            return predicates;
        };
    }
}