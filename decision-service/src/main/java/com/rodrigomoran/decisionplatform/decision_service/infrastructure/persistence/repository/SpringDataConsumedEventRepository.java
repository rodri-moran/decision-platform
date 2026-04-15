package com.rodrigomoran.decisionplatform.decision_service.infrastructure.persistence.repository;

import com.rodrigomoran.decisionplatform.decision_service.infrastructure.persistence.entity.ConsumedEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface SpringDataConsumedEventRepository extends JpaRepository<ConsumedEventEntity, Long> {
    boolean existsByEventId(UUID eventId);
}
