package com.rodrigomoran.decisionplatform.rules_service.infraestructure.persistence.repositories;

import com.rodrigomoran.decisionplatform.rules_service.domain.enums.RuleDraftStatus;
import com.rodrigomoran.decisionplatform.rules_service.infraestructure.entities.RuleDraft;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RuleDraftRepository extends JpaRepository<RuleDraft, Long> {

    boolean existsByRuleKey(String ruleKey);

    Optional<RuleDraft>findByRuleKey(String ruleKey);

    Page<RuleDraft> findByStatus(RuleDraftStatus status, Pageable pageable);

    boolean existsByRuleKeyAndStatus(String ruleKey, RuleDraftStatus status);
}
