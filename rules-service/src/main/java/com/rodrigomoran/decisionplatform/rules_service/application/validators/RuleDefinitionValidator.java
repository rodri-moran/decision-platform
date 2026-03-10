package com.rodrigomoran.decisionplatform.rules_service.application.validators;


import com.rodrigomoran.decisionplatform.rules_service.application.simulation.model.ParsedRuleDefinition;
import com.rodrigomoran.decisionplatform.rules_service.application.simulation.model.RuleDefinitionParser;
import org.springframework.stereotype.Component;

@Component
public class RuleDefinitionValidator {

    private final RuleDefinitionParser parser;

    public RuleDefinitionValidator(RuleDefinitionParser parser) {
        this.parser = parser;
    }

    public void validateOrThrow(ParsedRuleDefinition parsed) {
        if (parsed == null) {
            throw new IllegalArgumentException("Definition must not be null");
        }

        if (parsed.getWhen() == null || parsed.getWhen().isEmpty()) {
            throw new IllegalArgumentException("Rule definition must contain at least one condition");
        }

        parsed.getWhen().forEach(condition -> {
            if (condition.getField() == null || condition.getField().isBlank()) {
                throw new IllegalArgumentException("Condition field must not be null or blank");
            }
            if (condition.getOp() == null || condition.getOp().isBlank()) {
                throw new IllegalArgumentException("Condition operator must not be null or blank");
            }
            if (condition.getValue() == null) {
                throw new IllegalArgumentException("Condition value must not be null");
            }
        });
    }
}