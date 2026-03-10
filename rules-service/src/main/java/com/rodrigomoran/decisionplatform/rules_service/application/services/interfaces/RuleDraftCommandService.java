package com.rodrigomoran.decisionplatform.rules_service.application.services.interfaces;

import com.rodrigomoran.decisionplatform.rules_service.interfaces.dtos.CreateRuleDraftRequestDTO;
import com.rodrigomoran.decisionplatform.rules_service.interfaces.dtos.RuleDraftResponseDTO;
import com.rodrigomoran.decisionplatform.rules_service.interfaces.dtos.UpdateRuleDraftRequestDTO;
import jakarta.transaction.Transactional;

public interface RuleDraftCommandService {
    @Transactional
    RuleDraftResponseDTO createDraft(CreateRuleDraftRequestDTO request, String actor, String traceId);
    @Transactional
    RuleDraftResponseDTO updateDraft(Long draftd, UpdateRuleDraftRequestDTO request, String actor, String traceId);
    @Transactional
    void archiveDraft(Long draftId, String actor, String traceId);
}