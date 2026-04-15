package com.rodrigomoran.decisionplatform.decision_service.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RuleCondition {
    private String field;
    private ConditionOperator operator;
    private Object value;
}
