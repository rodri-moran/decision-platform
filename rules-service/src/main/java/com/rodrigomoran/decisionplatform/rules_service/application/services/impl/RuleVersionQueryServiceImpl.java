package com.rodrigomoran.decisionplatform.rules_service.application.services.impl;

import com.rodrigomoran.decisionplatform.rules_service.application.services.interfaces.RuleVersionQueryService;
import com.rodrigomoran.decisionplatform.rules_service.interfaces.dtos.RuleVersionResponseDTO;

import java.util.List;

public class RuleVersionQueryServiceImpl implements RuleVersionQueryService {
    @Override
    public RuleVersionResponseDTO getActiveVersion(String ruleKey) {

        return null;
    }
    @Override
    public RuleVersionResponseDTO getVersion(String ruleKey, Integer versionNumber) {

        return null;
    }
    @Override
    public List<RuleVersionResponseDTO> listVersions(String ruleKey, int page, int size) {

        return null;
    }
}