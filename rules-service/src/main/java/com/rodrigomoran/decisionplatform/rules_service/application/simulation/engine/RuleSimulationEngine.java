package com.rodrigomoran.decisionplatform.rules_service.application.simulation.engine;

import com.rodrigomoran.decisionplatform.rules_service.application.simulation.model.ConditionEvaluationResult;
import com.rodrigomoran.decisionplatform.rules_service.application.simulation.model.ContextValueResolver;
import com.rodrigomoran.decisionplatform.rules_service.application.simulation.model.ParsedRuleDefinition;
import com.rodrigomoran.decisionplatform.rules_service.application.simulation.model.RuleCondition;
import com.rodrigomoran.decisionplatform.rules_service.domain.model.RuleConditionGroup;
import com.rodrigomoran.decisionplatform.rules_service.domain.model.RuleConditionGroup;
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

    /*
     * Punto de entrada principal del simulador.
     *
     * Ahora ya no usamos:
     * - logic
     * - when
     *
     * Sino:
     * - operator
     * - conditions
     * - groups
     */
    public boolean evaluate(ParsedRuleDefinition definition,
                            Map<String, Object> sampleContext,
                            List<String> reasons) {

        return evaluateDefinition(
                definition.getOperator(),
                definition.getConditions(),
                definition.getGroups(),
                sampleContext,
                reasons
        );
    }

    /*
     * Evalúa un bloque de regla:
     * - condiciones simples
     * - grupos anidados
     * - operador lógico AND / OR
     */
    private boolean evaluateDefinition(String operator,
                                       List<RuleCondition> conditions,
                                       List<RuleConditionGroup> groups,
                                       Map<String, Object> sampleContext,
                                       List<String> reasons) {

        List<Boolean> results = new ArrayList<>();

        if (conditions != null) {
            for (RuleCondition condition : conditions) {
                ConditionEvaluationResult result = evaluateCondition(condition, sampleContext);
                results.add(result.isMatched());
                reasons.add(result.getReason());
            }
        }

        if (groups != null) {
            for (RuleConditionGroup group : groups) {
                boolean groupResult = evaluateGroup(group, sampleContext, reasons);
                results.add(groupResult);
            }
        }

        if (results.isEmpty()) {
            throw new IllegalArgumentException("Rule definition must contain at least one condition or group");
        }

        String normalizedOperator = operator == null ? "AND" : operator.toUpperCase();

        return switch (normalizedOperator) {
            case "AND" -> results.stream().allMatch(Boolean::booleanValue);
            case "OR" -> results.stream().anyMatch(Boolean::booleanValue);
            default -> throw new IllegalArgumentException("Unsupported logical operator: " + normalizedOperator);
        };
    }

    /*
     * Evalúa recursivamente un grupo anidado.
     */
    private boolean evaluateGroup(RuleConditionGroup group,
                                  Map<String, Object> sampleContext,
                                  List<String> reasons) {
        return evaluateDefinition(
                group.getOperator(),
                group.getConditions(),
                group.getGroups(),
                sampleContext,
                reasons
        );
    }

    /*
     * Evalúa una condición simple.
     */
    private ConditionEvaluationResult evaluateCondition(RuleCondition condition,
                                                        Map<String, Object> sampleContext) {
        Object actualValue = contextValueResolver.resolve(sampleContext, condition.getField());
        Object expectedValue = condition.getValue();

        ComparisonOperator comparisonOperator = ComparisonOperator.fromName(condition.getOperator());

        boolean matched;
        String reason;

        try {
            matched = comparisonOperator.evaluate(actualValue, expectedValue);
            reason = buildReason(condition, actualValue, matched);
        } catch (Exception e) {
            matched = false;
            reason = "Error evaluating condition %s %s %s. Error: %s"
                    .formatted(condition.getField(), condition.getOperator(), expectedValue, e.getMessage());
        }

        return new ConditionEvaluationResult(matched, reason);
    }

    /*
     * Arma la explicación textual de cada condición.
     */
    private String buildReason(RuleCondition condition, Object actualValue, boolean matched) {
        String conditionText = "%s %s %s".formatted(
                condition.getField(),
                condition.getOperator(),
                condition.getValue()
        );

        if (matched) {
            return "Condition matched: %s. Actual value: %s".formatted(conditionText, actualValue);
        }

        return "Condition did not match: %s. Actual value: %s".formatted(conditionText, actualValue);
    }
}