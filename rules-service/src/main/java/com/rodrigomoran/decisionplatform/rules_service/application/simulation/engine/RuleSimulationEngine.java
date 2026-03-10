package com.rodrigomoran.decisionplatform.rules_service.application.simulation.engine;

import com.rodrigomoran.decisionplatform.rules_service.application.simulation.model.ConditionEvaluationResult;
import com.rodrigomoran.decisionplatform.rules_service.application.simulation.model.ContextValueResolver;
import com.rodrigomoran.decisionplatform.rules_service.application.simulation.model.ParsedRuleDefinition;
import com.rodrigomoran.decisionplatform.rules_service.application.simulation.model.RuleCondition;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class RuleSimulationEngine {

    private final ContextValueResolver contextValueResolver;

    public RuleSimulationEngine(ContextValueResolver contextValueResolver) {
        this.contextValueResolver = contextValueResolver;
    }

    public boolean evaluate(ParsedRuleDefinition definition,
                            Map<String, Object> sampleContext,
                            List<String> reasons) {

        List<RuleCondition> conditions = definition.getWhen();

        if (conditions == null || conditions.isEmpty()) {
            throw new IllegalArgumentException("Rule definition must contain at least one condition");
        }

        String logic = definition.getLogic() == null ? "AND" : definition.getLogic().toUpperCase();

        List<Boolean> results = new ArrayList<>();

        for (RuleCondition condition : conditions) {
            ConditionEvaluationResult result = evaluateCondition(condition, sampleContext);
            results.add(result.isMatched());
            reasons.add(result.getReason());
        }

        return switch (logic) {
            case "AND" -> results.stream().allMatch(Boolean::booleanValue);
            case "OR" -> results.stream().anyMatch(Boolean::booleanValue);
            default -> throw new IllegalArgumentException("Unsupported logic operator: " + logic);
        };
    }

    private ConditionEvaluationResult evaluateCondition(RuleCondition condition, Map<String, Object> sampleContext) {
        Object actualValue = contextValueResolver.resolve(sampleContext, condition.getField());
        Object expectedValue = condition.getValue();

        ComparisonOperator operator = ComparisonOperator.fromSymbol(condition.getOp());

        boolean matched;
        String reason;

        try {
            matched = operator.evaluate(actualValue, expectedValue);
            reason = buildReason(condition, actualValue, matched);
        } catch (Exception e) {
            matched = false;
            reason = "Error evaluating condition %s %s %s. Error: %s"
                    .formatted(condition.getField(), condition.getOp(), expectedValue, e.getMessage());
        }

        return new ConditionEvaluationResult(matched, reason);
    }

    private String buildReason(RuleCondition condition, Object actualValue, boolean matched) {
        String conditionText = "%s %s %s".formatted(
                condition.getField(),
                condition.getOp(),
                condition.getValue()
        );

        if (matched) {
            return "Condition matched: %s. Actual value: %s".formatted(conditionText, actualValue);
        }

        return "Condition did not match: %s. Actual value: %s".formatted(conditionText, actualValue);
    }
}
