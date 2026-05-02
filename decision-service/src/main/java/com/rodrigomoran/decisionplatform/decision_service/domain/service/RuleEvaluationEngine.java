package com.rodrigomoran.decisionplatform.decision_service.domain.service;
import com.rodrigomoran.decisionplatform.decision_service.domain.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

@Component
@Slf4j
public class RuleEvaluationEngine {

    public DecisionResult evaluate(ActiveRule activeRule,
                                   ParsedRuleDefinition definition,
                                   EvaluationContext context) {

        List<String> reasons = new ArrayList<>();
        boolean result = evaluateDefinition(definition, context, reasons);

        DecisionResult decisionResult = new DecisionResult();
        decisionResult.setResult(result);
        decisionResult.setReasons(reasons);
        decisionResult.setRuleKey(activeRule.getRuleKey());
        decisionResult.setVersion(activeRule.getVersion());
        decisionResult.setTraceId(activeRule.getTraceId());
        decisionResult.setEvaluatedAt(Instant.now());

        return decisionResult;
    }

    private boolean evaluateDefinition(ParsedRuleDefinition definition,
                                       EvaluationContext context,
                                       List<String> reasons) {
        List<Boolean> results = new ArrayList<>();

        for (RuleCondition condition : definition.getConditions()) {
            results.add(evaluateCondition(condition, context, reasons));
        }

        for (RuleConditionGroup group : definition.getGroups()) {
            results.add(evaluateGroup(group, context, reasons));
        }

        return applyLogicalOperator(definition.getOperator(), results);
    }

    private boolean evaluateGroup(RuleConditionGroup group,
                                  EvaluationContext context,
                                  List<String> reasons) {
        List<Boolean> results = new ArrayList<>();

        for (RuleCondition condition : group.getConditions()) {
            results.add(evaluateCondition(condition, context, reasons));
        }

        for (RuleConditionGroup childGroup : group.getGroups()) {
            results.add(evaluateGroup(childGroup, context, reasons));
        }

        return applyLogicalOperator(group.getOperator(), results);
    }

    private boolean evaluateCondition(RuleCondition condition,
                                      EvaluationContext context,
                                      List<String> reasons) {

        Object actualValue = resolveNestedValue(context.getData(), condition.getField());
        log.info("Actual value: {} EvaluationContext: {}",actualValue, context);
        Object expectedValue = condition.getValue();

        boolean matched = switch (condition.getOperator()) {
            case EQUALS -> Objects.equals(actualValue, expectedValue);
            case NOT_EQUALS -> !Objects.equals(actualValue, expectedValue);
            case GREATER_THAN -> compareAsNumbers(actualValue, expectedValue) > 0;
            case GREATER_THAN_OR_EQUALS -> compareAsNumbers(actualValue, expectedValue) >= 0;
            case LESS_THAN -> compareAsNumbers(actualValue, expectedValue) < 0;
            case LESS_THAN_OR_EQUALS -> compareAsNumbers(actualValue, expectedValue) <= 0;
            case CONTAINS -> contains(actualValue, expectedValue);
        };

        if (matched) {
            reasons.add("Condition matched: field=%s, operator=%s, expected=%s, actual=%s"
                    .formatted(condition.getField(), condition.getOperator(), expectedValue, actualValue));
        } else {
            reasons.add("Condition did not match: field=%s, operator=%s, expected=%s, actual=%s"
                    .formatted(condition.getField(), condition.getOperator(), expectedValue, actualValue));
        }

        return matched;
    }

    private Object resolveNestedValue(Map<String, Object> data, String fieldPath) {
        if (data == null || fieldPath == null || fieldPath.isBlank()) {
            return null;
        }

        String[] parts = fieldPath.split("\\.");
        Object current = data;

        for (String part : parts) {
            if (!(current instanceof Map<?, ?> currentMap)) {
                return null;
            }
            current = currentMap.get(part);
        }

        return current;
    }

    private boolean applyLogicalOperator(LogicalOperator operator, List<Boolean> results) {
        if (results.isEmpty()) {
            return false;
        }

        return switch (operator) {
            case AND -> results.stream().allMatch(Boolean::booleanValue);
            case OR -> results.stream().anyMatch(Boolean::booleanValue);
        };
    }

    private int compareAsNumbers(Object actualValue, Object expectedValue) {
        if (actualValue == null || expectedValue == null) {
            return -1;
        }

        BigDecimal actual = toBigDecimal(actualValue);
        BigDecimal expected = toBigDecimal(expectedValue);

        return actual.compareTo(expected);
    }

    private BigDecimal toBigDecimal(Object value) {
        return new BigDecimal(String.valueOf(value));
    }

    private boolean contains(Object actualValue, Object expectedValue) {
        if (actualValue == null || expectedValue == null) {
            return false;
        }

        if (actualValue instanceof String text) {
            return text.contains(String.valueOf(expectedValue));
        }

        if (actualValue instanceof Collection<?> collection) {
            return collection.contains(expectedValue);
        }

        return false;
    }
}