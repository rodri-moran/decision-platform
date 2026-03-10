package com.rodrigomoran.decisionplatform.rules_service.application.services.interfaces;

import com.rodrigomoran.decisionplatform.rules_service.interfaces.dtos.RuleVersionResponseDTO;
import jakarta.transaction.Transactional;

public interface RuleVersionCommandService {

    @Transactional
    RuleVersionResponseDTO rollbackToVersion(String ruleKey, Integer targetVersionNumber, String actor, String traceId, String idempotencyKey);
}