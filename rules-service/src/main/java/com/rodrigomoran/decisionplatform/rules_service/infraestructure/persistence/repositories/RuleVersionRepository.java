package com.rodrigomoran.decisionplatform.rules_service.infraestructure.persistence.repositories;

import com.rodrigomoran.decisionplatform.rules_service.domain.enums.RuleVersionStatus;
import com.rodrigomoran.decisionplatform.rules_service.infraestructure.entities.RuleVersion;
import io.micrometer.core.instrument.config.validate.Validated;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RuleVersionRepository extends JpaRepository<RuleVersion, Long> {
    Optional<RuleVersion> findTopByRuleKeyOrderByVersionNumberDesc(String ruleKey);
    Optional<RuleVersion> findByRuleKeyAndVersionNumber(String ruleKey, Integer versionNumber);
    Optional<RuleVersion> findByRuleKeyAndStatus(String ruleKey, RuleVersionStatus status);
    Page<RuleVersion> findAllByRuleKey(String ruleKey, Pageable pageable);
    Optional<RuleVersion> findByRuleKey(String ruleKey);
}