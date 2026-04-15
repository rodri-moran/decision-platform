package com.rodrigomoran.decisionplatform.rules_service.application.validators;

import com.rodrigomoran.decisionplatform.rules_service.application.simulation.model.ParsedRuleDefinition;
import com.rodrigomoran.decisionplatform.rules_service.application.simulation.model.RuleCondition;
import com.rodrigomoran.decisionplatform.rules_service.domain.model.RuleConditionGroup;
import org.springframework.stereotype.Component;

@Component
public class RuleDefinitionValidator {

    public void validateOrThrow(ParsedRuleDefinition parsed) {
        if (parsed == null) {
            throw new IllegalArgumentException("Definition must not be null");
        }

        if (parsed.getOperator() == null || parsed.getOperator().isBlank()) {
            throw new IllegalArgumentException("Rule operator must not be null or blank");
        }

        boolean hasConditions = parsed.getConditions() != null && !parsed.getConditions().isEmpty();
        boolean hasGroups = parsed.getGroups() != null && !parsed.getGroups().isEmpty();

        if (!hasConditions && !hasGroups) {
            throw new IllegalArgumentException("Rule definition must contain at least one condition or group");
        }

        if (hasConditions) {
            parsed.getConditions().forEach(this::validateCondition);
        }

        if (hasGroups) {
            parsed.getGroups().forEach(this::validateGroup);
        }
    }

    private void validateCondition(RuleCondition condition) {
        if (condition == null) {
            throw new IllegalArgumentException("Condition must not be null");
        }

        if (condition.getField() == null || condition.getField().isBlank()) {
            throw new IllegalArgumentException("Condition field must not be null or blank");
        }

        if (condition.getOperator() == null || condition.getOperator().isBlank()) {
            throw new IllegalArgumentException("Condition operator must not be null or blank");
        }

        if (condition.getValue() == null) {
            throw new IllegalArgumentException("Condition value must not be null");
        }
    }

    private void validateGroup(RuleConditionGroup group) {
        if (group == null) {
            throw new IllegalArgumentException("Condition group must not be null");
        }

        if (group.getOperator() == null || group.getOperator().isBlank()) {
            throw new IllegalArgumentException("Group operator must not be null or blank");
        }

        boolean hasConditions = group.getConditions() != null && !group.getConditions().isEmpty();
        boolean hasSubGroups = group.getGroups() != null && !group.getGroups().isEmpty();

        if (!hasConditions && !hasSubGroups) {
            throw new IllegalArgumentException("Group must contain at least one condition or subgroup");
        }

        if (hasConditions) {
            group.getConditions().forEach(this::validateCondition);
        }

        if (hasSubGroups) {
            group.getGroups().forEach(this::validateGroup);
        }
    }
}