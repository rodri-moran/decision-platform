package com.rodrigomoran.decisionplatform.rules_service.infraestructure.persistence.repositories;

import com.rodrigomoran.decisionplatform.rules_service.infraestructure.entities.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OutboxEventRepository extends JpaRepository<OutboxEvent, Long> {
}
