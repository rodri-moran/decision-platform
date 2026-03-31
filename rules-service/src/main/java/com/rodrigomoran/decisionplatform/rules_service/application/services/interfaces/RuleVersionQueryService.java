package com.rodrigomoran.decisionplatform.rules_service.application.services.interfaces;

import com.rodrigomoran.decisionplatform.rules_service.interfaces.dtos.PageResponseDto;
import com.rodrigomoran.decisionplatform.rules_service.interfaces.dtos.RuleVersionResponseDTO;

import java.util.List;

public interface RuleVersionQueryService {
    RuleVersionResponseDTO getActiveVersion(String ruleKey);

    RuleVersionResponseDTO getVersion(String ruleKey, Integer versionNumber);

    PageResponseDto<RuleVersionResponseDTO> listVersions(String ruleKey, int page, int size);
}
