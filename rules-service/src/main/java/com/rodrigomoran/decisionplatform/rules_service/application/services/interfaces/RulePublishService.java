package com.rodrigomoran.decisionplatform.rules_service.application.services.interfaces;

import com.rodrigomoran.decisionplatform.rules_service.interfaces.dtos.RuleVersionResponseDTO;
import jakarta.transaction.Transactional;

public interface RulePublishService {

    @Transactional
    RuleVersionResponseDTO publishDraft(Long draftId, String actor, String traceId, String idempotencyKey);
}