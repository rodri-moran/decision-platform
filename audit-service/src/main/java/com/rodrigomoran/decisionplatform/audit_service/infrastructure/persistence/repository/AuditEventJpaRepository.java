package com.rodrigomoran.decisionplatform.audit_service.infrastructure.persistence.repository;
import com.rodrigomoran.decisionplatform.audit_service.infrastructure.persistence.entity.AuditEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface AuditEventJpaRepository extends JpaRepository<AuditEventEntity, UUID>, JpaSpecificationExecutor<AuditEventEntity> {
    boolean existsByEventId(String eventId);
    List<AuditEventEntity> findByTraceIdOrderByOccurredAtAsc(String traceId);
}