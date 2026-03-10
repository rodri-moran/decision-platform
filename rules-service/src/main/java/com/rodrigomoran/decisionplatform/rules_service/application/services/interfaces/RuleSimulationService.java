package com.rodrigomoran.decisionplatform.rules_service.application.services.interfaces;

import com.rodrigomoran.decisionplatform.rules_service.interfaces.dtos.RuleSimulationRequestDTO;
import com.rodrigomoran.decisionplatform.rules_service.interfaces.dtos.RuleSimulationResultDTO;

import java.util.Map;

public interface RuleSimulationService {

    RuleSimulationResultDTO simulateDefinition(RuleSimulationRequestDTO request, String traceId);

    RuleSimulationResultDTO simulateActiveRule(String ruleKey, Map<String, Object> sampleContext, String traceId);
}