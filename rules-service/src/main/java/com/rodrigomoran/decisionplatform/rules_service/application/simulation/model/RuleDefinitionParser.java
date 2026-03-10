package com.rodrigomoran.decisionplatform.rules_service.application.simulation.model;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class RuleDefinitionParser {

    private final ObjectMapper objectMapper;

    public ParsedRuleDefinition parse(String definitionJson) {
        try {
            return objectMapper.readValue(definitionJson, ParsedRuleDefinition.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid rule definition JSON", e);
        }
    }
}