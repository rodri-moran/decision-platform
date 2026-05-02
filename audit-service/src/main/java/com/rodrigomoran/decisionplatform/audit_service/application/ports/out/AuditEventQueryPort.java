package com.rodrigomoran.decisionplatform.audit_service.application.ports.out;
import com.rodrigomoran.decisionplatform.audit_service.domain.model.AuditSearchCriteria;
import com.rodrigomoran.decisionplatform.audit_service.infrastructure.persistence.entity.AuditEventEntity;
import org.springframework.data.domain.Page;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuditEventQueryPort {

    Optional<AuditEventEntity> findById(UUID id);
    List<AuditEventEntity> findByTraceId(String traceId);
    Page<AuditEventEntity> search(AuditSearchCriteria criteria);
}