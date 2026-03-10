package com.rodrigomoran.decisionplatform.rules_service.application.services.impl;

import com.rodrigomoran.decisionplatform.rules_service.application.services.interfaces.RuleDraftQueryService;
import com.rodrigomoran.decisionplatform.rules_service.domain.enums.RuleDraftStatus;
import com.rodrigomoran.decisionplatform.rules_service.interfaces.dtos.RuleDraftResponseDTO;
import java.util.List;
public class RuleDraftQueryServiceImpl implements RuleDraftQueryService {
    @Override
    public RuleDraftResponseDTO getDraftById(Long draftId) {

        return null;
    }
    @Override
    public RuleDraftResponseDTO getDraftByRuleKey(String ruleKey) {

        return null;
    }
    @Override
    public List<RuleDraftResponseDTO> listDrafts(RuleDraftStatus status, int page, int size) {

        return null;
    }
}