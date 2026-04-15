package com.rodrigomoran.decisionplatform.decision_service.infrastructure.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodrigomoran.decisionplatform.decision_service.application.exception.InvalidRuleDefinitionException;
import com.rodrigomoran.decisionplatform.decision_service.domain.model.ParsedRuleDefinition;
import com.rodrigomoran.decisionplatform.decision_service.domain.port.out.RuleDefinitionParserPort;

public class JacksonRuleDefinitionParserAdapter implements RuleDefinitionParserPort {

    private final ObjectMapper objectMapper;

    public JacksonRuleDefinitionParserAdapter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public ParsedRuleDefinition parse(String definitionJson) {
        try {
            return objectMapper.readValue(definitionJson, ParsedRuleDefinition.class);
        } catch (Exception ex) {
            throw new InvalidRuleDefinitionException("Invalid rule definition JSON", ex);
        }
    }
}
