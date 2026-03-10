package com.rodrigomoran.decisionplatform.rules_service.infraestructure.persistence.repositories;

import com.rodrigomoran.decisionplatform.rules_service.infraestructure.entities.RuleDraft;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RuleDraftRepository extends JpaRepository<RuleDraft, Long> {

    boolean existsByRuleKey(String ruleKey);
}
