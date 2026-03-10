package com.rodrigomoran.decisionplatform.rules_service.application.services.impl;

import com.rodrigomoran.decisionplatform.rules_service.application.services.interfaces.RulePublishService;
import com.rodrigomoran.decisionplatform.rules_service.interfaces.dtos.RuleVersionResponseDTO;
import jakarta.transaction.Transactional;

public class RulePublishServiceImpl implements RulePublishService {
    @Override
    @Transactional
    public RuleVersionResponseDTO publishDraft(Long draftId, String actor, String traceId, String idempotencyKey) {

        return null;
    }
}
