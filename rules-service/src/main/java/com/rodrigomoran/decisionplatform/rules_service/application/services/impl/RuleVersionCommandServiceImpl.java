package com.rodrigomoran.decisionplatform.rules_service.application.services.impl;

import com.rodrigomoran.decisionplatform.rules_service.application.services.interfaces.RuleVersionCommandService;
import com.rodrigomoran.decisionplatform.rules_service.interfaces.dtos.RuleVersionResponseDTO;
import jakarta.transaction.Transactional;

public class RuleVersionCommandServiceImpl implements RuleVersionCommandService {
    @Override
    @Transactional
    public RuleVersionResponseDTO rollbackToVersion(String ruleKey, Integer targetVersionNumber, String actor, String traceId, String idempotencyKey) {

        return null;
    }
}
