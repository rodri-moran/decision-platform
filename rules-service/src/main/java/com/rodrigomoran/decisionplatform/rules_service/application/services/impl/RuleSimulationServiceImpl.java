package com.rodrigomoran.decisionplatform.rules_service.application.services.impl;

import com.rodrigomoran.decisionplatform.rules_service.application.services.interfaces.RuleSimulationService;
import com.rodrigomoran.decisionplatform.rules_service.application.simulation.engine.RuleSimulationEngine;
import com.rodrigomoran.decisionplatform.rules_service.application.simulation.model.ParsedRuleDefinition;
import com.rodrigomoran.decisionplatform.rules_service.application.simulation.model.RuleDefinitionParser;
import com.rodrigomoran.decisionplatform.rules_service.application.validators.RuleDefinitionValidator;
import com.rodrigomoran.decisionplatform.rules_service.interfaces.dtos.RuleSimulationRequestDTO;
import com.rodrigomoran.decisionplatform.rules_service.interfaces.dtos.RuleSimulationResultDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
@Service
@RequiredArgsConstructor
public class RuleSimulationServiceImpl implements RuleSimulationService {
    private final RuleDefinitionValidator validator;
    private final RuleDefinitionParser parser;
    private final RuleSimulationEngine ruleSimulationEngine;
    @Override
    public RuleSimulationResultDTO simulateDefinition(RuleSimulationRequestDTO request, String traceId) {
        if(Objects.isNull(request)){
            throw new IllegalArgumentException("Request must not be null");
        }
        if(Objects.isNull(request.getDefinition())){
            throw new IllegalArgumentException("Definition must not be null");
        }
        if(Objects.isNull(request.getSampleContext())){
            throw new IllegalArgumentException("Sample context must not be null");
        }
        validator.validateOrThrow(request.getDefinition());
        List<String> reasons = new ArrayList<>();
        boolean matched = ruleSimulationEngine.evaluate(
                request.getDefinition(),
                request.getSampleContext(),
                reasons);
        return new RuleSimulationResultDTO(matched, reasons, traceId);
    }
    @Override
    public RuleSimulationResultDTO simulateActiveRule(String ruleKey, Map<String, Object> sampleContext, String traceId) {
        // TODO: Validar ruleKey

        // TODO: Validar sampleContext
        // TODO: Buscar versión ACTIVE por ruleKey
        // TODO: Si no existe, lanzar ActiveVersionNotFoundException
        // TODO: Tomar definitionJson de la versión activa
        // TODO: Reutilizar la lógica de simulateDefinition
        // TODO: Retornar resultado
        return null;
    }
}
