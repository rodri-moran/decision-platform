package com.rodrigomoran.decisionplatform.rules_service.infraestructure.persistence.repositories;
import com.rodrigomoran.decisionplatform.rules_service.domain.enums.IdempotencyOperationType;
import com.rodrigomoran.decisionplatform.rules_service.infraestructure.entities.Idempotency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface IdempotencyRepository extends JpaRepository<Idempotency, Long> {
    Optional<Idempotency> findByOperationTypeAndIdempotencyKey(IdempotencyOperationType operationType, String idempotencyKey);
}