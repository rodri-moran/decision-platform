package com.rodrigomoran.decisionplatform.decision_service.infrastructure.persistence.repository;

import com.rodrigomoran.decisionplatform.decision_service.infrastructure.persistence.entity.ActiveRuleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface SpringDataActiveRuleRepository extends JpaRepository<ActiveRuleEntity, Long> {
    Optional<ActiveRuleEntity> findByRuleKey(String ruleKey);
}
