package com.rodrigomoran.decisionplatform.decision_service.domain.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParsedRuleDefinition {
    private LogicalOperator operator;
    private List<RuleCondition> conditions = new ArrayList<>();
    private List<RuleConditionGroup> groups = new ArrayList<>();
}