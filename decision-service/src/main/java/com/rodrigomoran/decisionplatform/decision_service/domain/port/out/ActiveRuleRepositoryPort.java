package com.rodrigomoran.decisionplatform.decision_service.domain.port.out;

import com.rodrigomoran.decisionplatform.decision_service.domain.model.ActiveRule;

import java.util.Optional;

public interface ActiveRuleRepositoryPort {
    Optional<ActiveRule> findByRuleKey(String ruleKey);
    ActiveRule save(ActiveRule activeRule);
}
