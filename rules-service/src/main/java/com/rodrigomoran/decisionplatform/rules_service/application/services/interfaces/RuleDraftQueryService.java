package com.rodrigomoran.decisionplatform.rules_service.application.services.interfaces;
import com.rodrigomoran.decisionplatform.rules_service.domain.enums.RuleDraftStatus;
import com.rodrigomoran.decisionplatform.rules_service.interfaces.dtos.RuleDraftResponseDTO;
import java.util.List;
public interface RuleDraftQueryService {
    RuleDraftResponseDTO getDraftById(Long draftId);
    RuleDraftResponseDTO getDraftByRuleKey(String ruleKey);
    List<RuleDraftResponseDTO> listDrafts(RuleDraftStatus status, int page, int size);
}