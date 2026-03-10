package com.rodrigomoran.decisionplatform.rules_service.interfaces.dtos;

import com.rodrigomoran.decisionplatform.rules_service.application.simulation.model.ParsedRuleDefinition;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

public class RuleSimulationRequestDTO {

    private ParsedRuleDefinition definition;
    private Map<String, Object> sampleContext;

    public RuleSimulationRequestDTO() {
    }

    public ParsedRuleDefinition getDefinition() {
        return definition;
    }

    public void setDefinition(ParsedRuleDefinition definition) {
        this.definition = definition;
    }

    public Map<String, Object> getSampleContext() {
        return sampleContext;
    }

    public void setSampleContext(Map<String, Object> sampleContext) {
        this.sampleContext = sampleContext;
    }
}
