package com.rodrigomoran.decisionplatform.decision_service.domain.port.out;

import com.rodrigomoran.decisionplatform.decision_service.domain.model.ParsedRuleDefinition;

public interface RuleDefinitionParserPort {
    ParsedRuleDefinition parse(String definitionJson);
}
