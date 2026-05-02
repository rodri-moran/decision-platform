package com.rodrigomoran.decisionplatform.audit_service.application.ports.out;
import com.rodrigomoran.decisionplatform.audit_service.infrastructure.persistence.entity.AuditEventEntity;
public interface AuditEventPersistencePort {
    boolean existsByEventId(String eventId);
    AuditEventEntity save(AuditEventEntity entity);
}